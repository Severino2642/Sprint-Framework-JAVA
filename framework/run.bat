@echo off
set destination="..\Test\lib"
set name_project="sprint"

rem Compilation des fichiers Java
javac -cp .\lib\* -d .\temporary .\src\mg\itu\framework\sprint\controller\*.java

echo Création du fichier JAR
cd .\temporary
jar -cvf "%name_project%.jar" *
cd ..

echo Déplacement du fichier JAR vers le dossier général
move /y ".\temporary\%name_project%.jar" "%destination%"
