@echo off
setlocal

set MAVEN_PROJECTBASEDIR=%~dp0.
set WRAPPER_JAR=%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar

if not exist "%WRAPPER_JAR%" (
  echo Maven wrapper jar not found: %WRAPPER_JAR%
  echo Download it from the wrapperUrl in .mvn\wrapper\maven-wrapper.properties
  exit /b 1
)

java -Dmaven.multiModuleProjectDirectory="%MAVEN_PROJECTBASEDIR%" -classpath "%WRAPPER_JAR%" org.apache.maven.wrapper.MavenWrapperMain %*
