@echo off
cd app\release || exit
:: Install the APKs
echo Installing APKs...
bundletool install-apks --apks=counter.apks
echo APKs installed successfully.
echo Script completed.
