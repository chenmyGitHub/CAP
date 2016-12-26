@echo off
rem Change to current directory
cd /d %~dp0

cls
title Installing CAP Project 
rem call gradle clean


if ERRORLEVEL 1 GOTO :ERR
rem [断点调试]According to the plugin configuration, the tomcat7 runs in Gradle JVM environment, so appending JPDA debug options to  GRADLE_OPTS will work.
set GRADLE_OPTS=-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005
set DEFAULT_JVM_OPTS=-XX:+HeapDumpOnOutOfMemoryError -Xms128m -Xmx1024m  -Dfile.encoding=GBK
set JAVA_OPTS=-server -XX:PermSize=128M -XX:MaxPermSize=256m


:START
cls
rem Run the [tomcat7:run].
title Running CAP-Model Project in Tomcat 7
call gradle tomcatRun -x test


echo.
rem set /p var=Press ENTER to restart, OR click on the right top red button of the window.
set /p var=按回车键重启,或者点击窗口右上角红色按钮关闭窗口.
GOTO :START

:ERR
echo.
echo. CAP-Model Project Installation has failed. See above for details.
pause>nul
