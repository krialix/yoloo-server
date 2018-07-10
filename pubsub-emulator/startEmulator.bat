@echo off
title PubSub Emulator
echo Starting PubSub Emulator.

cmd /k "gcloud beta emulators pubsub start"
pause