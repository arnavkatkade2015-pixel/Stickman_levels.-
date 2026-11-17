@echo off
cd /d "%~dp0app\src\main\assets\libs"
curl -L -o three.min.js https://cdn.jsdelivr.net/npm/three@0.160.0/build/three.min.js
curl -L -o rapier.es.js https://cdn.jsdelivr.net/npm/@dimforge/rapier3d-compat@0.14.0/rapier.es.js
echo Done.
pause
