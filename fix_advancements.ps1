$files = Get-ChildItem `
    "src\main\resources\data\anglers_odyssey\advancements" `
    -Recurse `
    -Filter *.json

foreach ($file in $files) {

    $content = Get-Content $file.FullName -Raw

    $content = $content.Replace(
        '"trigger": "minecraft:fishing_rod_hooked"',
        '"trigger": "minecraft:inventory_changed"'
    )

    Set-Content $file.FullName $content -Encoding UTF8

    Write-Host "Updated $($file.Name)"
}

Write-Host ""
Write-Host "Finished."