@echo off
setlocal enabledelayedexpansion

cd .\src

set "dossier_principal=.\"
set "dossier_temp=temp"

if not exist "%dossier_temp%" mkdir "%dossier_temp%"

for /r "%dossier_principal%" %%f in (*.java) do (
    set "chemin_complet=%%~f"
    set "nom_fichier=%%~nf.java"
    
    copy "!chemin_complet!" "%dossier_temp%\!nom_fichier!" > nul
)

set projet=sprint-framework
set src=temp\*.java
set mainPkg=.\mg
set archive=%projet%.jar

javac -d %dossier_temp% %src%
cd %dossier_temp%
jar -cf %archive% .\mg
cd ..
xcopy "temp\%archive%" "%dossier_principal%" /y

@REM Command to copy the jar to Test librairie
@REM set destination=..\Test\lib
@REM if exist "%destination%%archive%" (
@REM     del /S /Q "%destination%%archive%"
@REM )
@REM xcopy "%archive%" "%destination%" /y

rem rmdir /S /Q %dossier_temp%

endlocal