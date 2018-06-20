#!/bin/bash
#
# description: Startup script for zkdoctor
#

ZKDOCTORDIR=/usr/local/zkdoctor
ZKDOCTORLOGDIR=${ZKDOCTORDIR}/logs
ZKDOCTORWAR=${ZKDOCTORDIR}/zkdoctor.war

# get zkdoctor pid
zkdoctor_pid() {
   echo `ps aux | grep ${ZKDOCTORWAR} | grep -v grep | awk '{ print $2 }'`
}

echo -n "Starting zkdoctor: "
if [ -n "$(zkdoctor_pid)" ];then
   echo "Start ERROR: The zkdoctor already started! Please check pid: $(zkdoctor_pid)"
   exit 1
fi

JAVA_OPTS="-server -Xms2048m -Xmx2048m -Xmn512m -Xss256k -XX:PermSize=256m -XX:MaxPermSize=256m -XX:SurvivorRatio=8 -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:+UseCMSCompactAtFullCollection -XX:+CMSParallelRemarkEnabled -XX:CMSInitiatingOccupancyFraction=70 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${ZKDOCTORLOGDIR}/zkdoctor.hprof"
nohup java $JAVA_OPTS -jar ${ZKDOCTORWAR} > ${ZKDOCTORLOGDIR}/stdout.log 2>&1 &

if [ -n "$(zkdoctor_pid)" ];then
   echo "Start SUCCESS! PID: $(zkdoctor_pid)"
else
   echo "Start FAILED! Please check logs!"
fi