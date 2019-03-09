@echo off

::服务名
set serverName=xiaoyi
::服务路径
set serverPath=C:\Users\win\Desktop\22s\start.bat

sc create %serverName% binPath=%serverPath% start=auto

pause