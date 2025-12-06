# PowerShell script to run the application
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
$PSDefaultParameterValues['Out-File:Encoding'] = 'utf8'

Write-Host "====================================" -ForegroundColor Green
Write-Host "   Quan Ly Day Them - HUE" -ForegroundColor Green  
Write-Host "====================================" -ForegroundColor Green
Write-Host ""

java "-Dfile.encoding=UTF-8" -jar target\QuanLyDayThem.jar

Write-Host ""
Read-Host "Press Enter to exit"
