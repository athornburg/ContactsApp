Evaluation Solution
===================

Pre-Reqs
---------
 1.     Built in Android Studio 0.5.4 with Java 1.7
 2.     Create local.properties file in root set variable sdk.dir to path to android sdk.
 3.     Make sure to download the latest version of Android build tools.
```
Example contents local.properties:
sdk.dir=/Users/alexthornburg/adt/sdk
```
 

Building from the Command Line
------------------------------
 1.     Change to project Directory.
```
chmod +x gradlew
./gradlew assembleDebug
```
