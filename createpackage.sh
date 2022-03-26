#!/bin/bash
thisdir="$(dirname "$(readlink -f "$0")")"
cd "$thisdir"
mazegameFile=mazegame.jar
mazegameFileDir=out/artifacts/mazegame_jar/"${mazegameFile}"

if [ ! -f "${mazegameFileDir}" ]; then
  echo "Can't find jar file at ${thisdir}/${mazegameFileDir}"
  exit 1
fi

if [ -d package ]; then
  rm -rf package
fi
mkdir package

cp "${mazegameFileDir}" package/
cp *.md package/
cp -r tilesets package/
cp -r example_mazes package/

cd package
zip -r ../mazegame.zip *
echo "Packaging finished"
