Add-Type -AssemblyName System.Drawing

$root = Split-Path -Parent $PSScriptRoot
$textureDir = Join-Path $root 'src\main\resources\assets\anglers_odyssey\textures\item'
$outputDir = Join-Path $root 'docs\curseforge'
$outputPath = Join-Path $outputDir 'fish-collection-showcase.png'

$excluded = @(
    'anglers_journal.png',
    'cooked_fish.png',
    'locked_fish.png',
    'reed_rod.png', 'reed_rod_cast.png',
    'oak_rod.png', 'oak_rod_cast.png',
    'ironwood_rod.png', 'ironwood_rod_cast.png',
    'anglers_rod.png', 'anglers_rod_cast.png'
)

$fishFiles = Get-ChildItem $textureDir -Filter '*.png' |
    Where-Object { $excluded -notcontains $_.Name } |
    Sort-Object BaseName

$columns = 12
$tile = 160
$header = 190
$rows = [Math]::Ceiling($fishFiles.Count / $columns)
$width = $columns * $tile
$height = $header + ($rows * $tile)

New-Item -ItemType Directory -Force $outputDir | Out-Null
$bitmap = New-Object System.Drawing.Bitmap($width, $height)
$graphics = [System.Drawing.Graphics]::FromImage($bitmap)
$graphics.Clear([System.Drawing.Color]::FromArgb(8, 31, 67))
$graphics.TextRenderingHint = [System.Drawing.Text.TextRenderingHint]::AntiAliasGridFit

$titleFont = New-Object System.Drawing.Font('Segoe UI', 34, [System.Drawing.FontStyle]::Bold)
$subtitleFont = New-Object System.Drawing.Font('Segoe UI', 16, [System.Drawing.FontStyle]::Regular)
$labelFont = New-Object System.Drawing.Font('Segoe UI', 10, [System.Drawing.FontStyle]::Regular)
$whiteBrush = New-Object System.Drawing.SolidBrush([System.Drawing.Color]::White)
$mutedBrush = New-Object System.Drawing.SolidBrush([System.Drawing.Color]::FromArgb(180, 211, 236))
$labelBrush = New-Object System.Drawing.SolidBrush([System.Drawing.Color]::FromArgb(225, 239, 250))
$goldBrush = New-Object System.Drawing.SolidBrush([System.Drawing.Color]::FromArgb(255, 194, 61))

$graphics.DrawString("ANGLER'S ODYSSEY", $titleFont, $whiteBrush, 38, 24)
$graphics.DrawString("151 FISH TO DISCOVER", $subtitleFont, $goldBrush, 42, 83)
$graphics.DrawString('Biome-based catches  |  Shiny variants  |  Trophy records', $subtitleFont, $mutedBrush, 42, 119)

$borderPen = New-Object System.Drawing.Pen([System.Drawing.Color]::FromArgb(35, 79, 119), 2)
$separatorPen = New-Object System.Drawing.Pen([System.Drawing.Color]::FromArgb(255, 194, 61), 3)
$graphics.DrawLine($separatorPen, 40, 166, ($width - 40), 166)

for ($index = 0; $index -lt $fishFiles.Count; $index++) {
    $file = $fishFiles[$index]
    $column = $index % $columns
    $row = [Math]::Floor($index / $columns)
    $x = $column * $tile
    $y = $header + ($row * $tile)
    $cellColor = if (($column + $row) % 2 -eq 0) { [System.Drawing.Color]::FromArgb(12, 46, 82) } else { [System.Drawing.Color]::FromArgb(15, 53, 92) }
    $cellBrush = New-Object System.Drawing.SolidBrush($cellColor)
    $graphics.FillRectangle($cellBrush, $x + 4, $y + 4, $tile - 8, $tile - 8)
    $graphics.DrawRectangle($borderPen, $x + 4, $y + 4, $tile - 8, $tile - 8)
    $cellBrush.Dispose()

    $image = [System.Drawing.Image]::FromFile($file.FullName)
    $maxSize = 102
    $scale = [Math]::Min($maxSize / $image.Width, $maxSize / $image.Height)
    $drawWidth = [Math]::Max(1, [int]($image.Width * $scale))
    $drawHeight = [Math]::Max(1, [int]($image.Height * $scale))
    $drawX = $x + (($tile - $drawWidth) / 2)
    $drawY = $y + 15 + (($maxSize - $drawHeight) / 2)
    $graphics.InterpolationMode = [System.Drawing.Drawing2D.InterpolationMode]::NearestNeighbor
    $graphics.PixelOffsetMode = [System.Drawing.Drawing2D.PixelOffsetMode]::Half
    $graphics.DrawImage($image, $drawX, $drawY, $drawWidth, $drawHeight)
    $image.Dispose()

    $label = ($file.BaseName -replace '_', ' ')
    $label = (Get-Culture).TextInfo.ToTitleCase($label)
    $labelSize = $graphics.MeasureString($label, $labelFont)
    $labelX = $x + (($tile - $labelSize.Width) / 2)
    $graphics.DrawString($label, $labelFont, $labelBrush, $labelX, $y + 123)
}

$bitmap.Save($outputPath, [System.Drawing.Imaging.ImageFormat]::Png)
$graphics.Dispose()
$bitmap.Dispose()
$titleFont.Dispose()
$subtitleFont.Dispose()
$labelFont.Dispose()
$whiteBrush.Dispose()
$mutedBrush.Dispose()
$labelBrush.Dispose()
$goldBrush.Dispose()
$borderPen.Dispose()
$separatorPen.Dispose()

Write-Output "Created $outputPath with $($fishFiles.Count) fish textures."
