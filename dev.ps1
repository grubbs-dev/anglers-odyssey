param(
    [ValidateSet('build', 'runClient', 'runServer')]
    [string]$Task = 'build'
)

$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$jdkRoot = Get-ChildItem (Join-Path $projectRoot '.tools\jdk21') -Directory -ErrorAction SilentlyContinue |
    Select-Object -First 1

if (-not $jdkRoot) {
    throw "Local Java 21 runtime not found under .tools\jdk21. Reinstall Temurin 21 or ask Codex to recreate the environment."
}

$env:JAVA_HOME = $jdkRoot.FullName
$env:PATH = "$($jdkRoot.FullName)\bin;$env:PATH"
$env:GRADLE_USER_HOME = Join-Path $projectRoot '.gradle-user'

Write-Host "Using Java: $(& (Join-Path $env:JAVA_HOME 'bin\java.exe') -version 2>&1 | Select-Object -First 1)"
Push-Location $projectRoot
try {
    & '.\gradlew.bat' $Task '--no-daemon'
    exit $LASTEXITCODE
}
finally {
    Pop-Location
}
