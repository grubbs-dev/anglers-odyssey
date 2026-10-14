$ErrorActionPreference = "Stop"

$versions = @(
    @("1.21", "1.21+build.9", "0.19.5", "0.102.0+1.21", "1.2.0"),
    @("1.21.1", "1.21.1+build.3", "0.19.5", "0.116.17+1.21.1", "1.2.1"),
    @("1.21.2", "1.21.2+build.1", "0.19.5", "0.106.1+1.21.2", "1.2.2"),
    @("1.21.3", "1.21.3+build.2", "0.19.5", "0.114.1+1.21.3", "1.2.3"),
    @("1.21.4", "1.21.4+build.8", "0.19.5", "0.119.4+1.21.4", "1.2.4"),
    @("1.21.5", "1.21.5+build.1", "0.19.5", "0.128.2+1.21.5", "1.2.5"),
    @("1.21.6", "1.21.6+build.1", "0.19.5", "0.128.2+1.21.6", "1.2.6"),
    @("1.21.7", "1.21.7+build.8", "0.19.5", "0.129.0+1.21.7", "1.2.7"),
    @("1.21.8", "1.21.8+build.1", "0.19.5", "0.136.1+1.21.8", "1.2.8"),
    @("1.21.9", "1.21.9+build.1", "0.19.5", "0.134.1+1.21.9", "1.2.9"),
    @("1.21.10", "1.21.10+build.3", "0.19.5", "0.138.4+1.21.10", "1.2.10"),
    @("1.21.11", "1.21.11+build.6", "0.19.5", "0.141.6+1.21.11", "1.2.11")
)

$gradleHome = $env:GRADLE_USER_HOME
if (-not $gradleHome) { $gradleHome = "V:\Development\Mods\GradleHome" }
$env:GRADLE_USER_HOME = $gradleHome
$results = @()

foreach ($version in $versions) {
    $minecraft = $version[0]
    $logPath = Join-Path (Get-Location) ("client-smoke-{0}.log" -f $minecraft)
    if (Test-Path -LiteralPath $logPath) { Remove-Item -LiteralPath $logPath -Force }

    $arguments = @(
        "/c", ".\gradlew.bat runClient --no-daemon --max-workers=1",
        "-Pminecraft_version=$($version[0])",
        "-Pyarn_mappings=$($version[1])",
        "-Ploader_version=$($version[2])",
        "-Pfabric_api_version=$($version[3])",
        "-Pmod_version=$($version[4])",
        "-Pjava_version=21",
        "> `"$logPath`" 2>&1"
    ) -join " "

    $process = Start-Process -FilePath "cmd.exe" -ArgumentList $arguments -WorkingDirectory (Get-Location) -PassThru -WindowStyle Hidden
    $deadline = (Get-Date).AddSeconds(150)
    $registered = $false
    while ((Get-Date) -lt $deadline -and -not $process.HasExited) {
        if (Test-Path -LiteralPath $logPath) {
            $text = Get-Content -LiteralPath $logPath -Raw -ErrorAction SilentlyContinue
            if ($text -match "Angler's Odyssey has loaded\." -and $text -match "Registered 151 Angler's Odyssey fish\.") {
                $registered = $true
                break
            }
        }
        Start-Sleep -Seconds 2
    }

    if (-not $process.HasExited) {
        $children = Get-CimInstance Win32_Process | Where-Object { $_.ParentProcessId -eq $process.Id }
        $children | ForEach-Object { Stop-Process -Id $_.ProcessId -Force -ErrorAction SilentlyContinue }
        Stop-Process -Id $process.Id -Force -ErrorAction SilentlyContinue
    }

    $text = if (Test-Path -LiteralPath $logPath) { Get-Content -LiteralPath $logPath -Raw } else { "" }
    $clientError = $text -match "Mixin transformation of|InjectionError|Critical injection failure|BUILD FAILED|Exception in thread"
    $result = [pscustomobject]@{ Version = $minecraft; Registered = $registered; ClientError = $clientError; Log = $logPath }
    $results += $result
    Write-Output ("{0}: registered={1}, clientError={2}" -f $minecraft, $registered, $clientError)
}

if ($results.Where({ -not $_.Registered -or $_.ClientError }).Count -gt 0) {
    exit 1
}
