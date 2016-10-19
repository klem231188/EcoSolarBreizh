#!/bin/bash

DATE=`date +%Y-%m-%d`
BASENAME="ecosolarbreizh"
FULLNAME=$BASENAME-$DATE.apk


cp app/build/outputs/apk/app-debug.apk delivery/$FULLNAME



