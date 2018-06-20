#!/bin/bash
#
# description: Stop script for zkdoctor
#

ZKDOCTORDIR=/usr/local/zkdoctor
ZKDOcTORWAR=${ZKDOCTORDIR}/zkdoctor.war

# get zkdoctor pid
zkdoctor_pids() {
   echo `ps aux | grep ${ZKDOcTORWAR} | grep -v grep | awk '{ print $2 }'`
}

if [ -z "$(zkdoctor_pids)" ]; then
    echo "The zkdoctor is not running!"
    exit 1
fi

echo -n "Stopping zkdoctor: "
for pid in $(zkdoctor_pids) ; do
    kill -9 ${pid} > /dev/null 2>&1
done

if [ -n "$(zkdoctor_pids)" ];then
    echo "Stop FAILED!"
else
    echo "Stop SUCCESS!"
fi