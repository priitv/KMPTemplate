#!/bin/bash

# Verify bash version. macOS comes with bash 3 preinstalled.
if [[ ${BASH_VERSINFO[0]} -lt 4 ]]
then
  echo "You need at least bash 4 to run this script."
  exit 1
fi

# exit when any command fails
set -e

if [[ $# -lt 2 ]]; then
   echo "Usage: bash customizer.sh my.new.package MyNewDataModel [ApplicationName]" >&2
   exit 2
fi

PACKAGE=$1
DATAMODEL=$2
APPNAME=$3
SUBDIR=${PACKAGE//.//} # Replaces . with /

for n in $(find . -type d \( \
    -path '*/src/main' -or \
    -path '*/src/androidTest' -or \
    -path '*/src/test' -or \
    -path '*/src/commonMain' -or \
    -path '*/src/commonTest' -or \
    -path '*/src/androidMain' -or \
    -path '*/src/androidInstrumentedTest' -or \
    -path '*/src/androidUnitTest' -or \
    -path '*/src/iosMain' -or \
    -path '*/src/iosArm64Main' -or \
    -path '*/src/iosSimulatorArm64Main' -or \
    -path '*/src/iosX64Main' \
  \) )
do
  if [ -d "$n/kotlin/android/template" ]; then
    echo "Creating $n/kotlin/$SUBDIR"
    mkdir -p $n/kotlin/$SUBDIR
    echo "Moving files to $n/kotlin/$SUBDIR"
    mv $n/kotlin/android/template/* $n/kotlin/$SUBDIR
    echo "Removing old $n/kotlin/android"
    rm -rf $n/kotlin/android
  fi
done

# Rename package and imports
echo "Renaming packages to $PACKAGE"
find ./ -type f -name "*.kt" -exec sed -i.bak "s/package android.template/package $PACKAGE/g" {} \;
find ./ -type f -name "*.kt" -exec sed -i.bak "s/import android.template/import $PACKAGE/g" {} \;

# Gradle files
find ./ -type f -name "*.kts" -exec sed -i.bak "s/android.template/$PACKAGE/g" {} \;

# Xcode config files
find ./ -type f -name "*.xcconfig" -exec sed -i.bak "s/android\.template/$PACKAGE/g" {} \;

# Rename model
echo "Renaming model to $DATAMODEL"
find ./ -type f -name "*.kt" -exec sed -i.bak "s/MyModel/${DATAMODEL^}/g" {} \; # First upper case
find ./ -type f -name "*.kt" -exec sed -i.bak "s/myModel/${DATAMODEL,}/g" {} \; # First lower case
find ./ -type f -name "*.kt*" -exec sed -i.bak "s/mymodel/${DATAMODEL,,}/g" {} \; # All lowercase

echo "Cleaning up"
find . -name "*.bak" -type f -delete

# Rename files
echo "Renaming files to $DATAMODEL"
find ./ -name "*MyModel*.kt" | sed "p;s/MyModel/${DATAMODEL^}/" | tr '\n' '\0' | xargs -0 -n 2 mv
# module names
if [[ -n $(find ./ -name "*-mymodel") ]]
then
  echo "Renaming modules to $DATAMODEL"
  find ./ -name "*-mymodel" -type d  | sed "p;s/mymodel/${DATAMODEL,,}/" |  tr '\n' '\0' | xargs -0 -n 2 mv
fi
# directories
echo "Renaming directories to $DATAMODEL"
find ./ -name "mymodel" -type d  | sed "p;s/mymodel/${DATAMODEL,,}/" |  tr '\n' '\0' | xargs -0 -n 2 mv

# Rename app
if [[ $APPNAME != MyApplication ]]
then
    echo "Renaming app to $APPNAME"
    find ./ -type f \( -name "MyApplication.kt" -or -name "settings.gradle.kts" -or -name "*.xml" \) -exec sed -i.bak "s/MyApplication/$APPNAME/g" {} \;
    find ./ -name "MyApplication.kt" | sed "p;s/MyApplication/$APPNAME/" | tr '\n' '\0' | xargs -0 -n 2 mv
    find . -name "*.bak" -type f -delete
fi

# Remove additional files
echo "Removing additional files"
rm -rf customizer.sh
rm -rf .git/
echo "Done!"
