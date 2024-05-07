@echo off
set "tomcat_path="C:\Tomcat\"
set "general_path=%tomcat_path%webapps\"
set "name_project=test_sprint"

rem Compilation des fichiers Java
javac -d .\temporary\WEB-INF\classes\ .\src\*.java

rem Copie des bibliothèques
xcopy .\lib .\temporary\WEB-INF\lib\ /e /s /y

rem Copie des fichiers de configuration
xcopy .\conf\ .\temporary\WEB-INF\ /y

echo copie des fichiers de web 
xcopy .\pages\ .\temporary\ /y

echo Création du fichier WAR
cd .\temporary
jar cvf "%name_project%.war" *
cd ..

echo Déplacement du fichier WAR vers le dossier général
set "dossier=%general_path%%name_project%"
set "fichier_war=%general_path%%name_project%.war"


move /y ".\temporary\%name_project%.war" "%general_path%"
rmdir /S /Q temporary