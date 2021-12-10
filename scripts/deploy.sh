#!/bin/bash
BUILD_JAR=$(ls /home/ec2-user/app/deploy/film-api/build/libs/*.jar)
JAR_NAME=$(basename $BUILD_JAR)
echo "> build file_name: $JAR_NAME" >> /home/ec2-user/app/deploy/deploy.log

echo "> build copy file" >> /home/ec2-user/app/deploy/deploy.log
DEPLOY_PATH=/home/ec2-user/app/deploy/
cp $BUILD_JAR $DEPLOY_PATH

echo "> pid check" >> /home/ec2-user/app/deploy/deploy.log
CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z $CURRENT_PID ]
then
  echo "> There is no application currently running, so do not exit." >> /home/ec2-user/app/deploy/deploy.log
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 15
fi

DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
echo "> DEPLOY_JAR deploy"    >> /home/ec2-user/app/deploy/deploy.log
nohup java -jar $DEPLOY_JAR >> /home/ec2-user/app/deploy/deploy.log 2>/home/ec2-user/app/deploy/deploy_err.log &
