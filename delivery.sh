#!/bin/bash

DATE=`date +%d-%m-%Y`
BASENAME="ecosolarbreizh"
FULLNAME=$BASENAME-$DATE.apk


cp app/build/outputs/apk/app-debug.apk delivery/$FULLNAME



