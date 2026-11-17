#!/bin/sh
# Downloads the two libraries the game needs so the app works with no internet.
cd "$(dirname "$0")/app/src/main/assets/libs" || exit 1
curl -L -o three.min.js https://cdn.jsdelivr.net/npm/three@0.160.0/build/three.min.js
curl -L -o rapier.es.js https://cdn.jsdelivr.net/npm/@dimforge/rapier3d-compat@0.14.0/rapier.es.js
echo Done.
