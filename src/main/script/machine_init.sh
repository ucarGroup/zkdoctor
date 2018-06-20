#!/bin/bash
#
# description: Init script for machine. Usage: sh machine_init.sh [username] [zooDir]
# [username]: user who can ssh the zookeeper server machine. Default: zkdoctor
# [zooDir]: the dir that zookeeper installed. Default: /user/local/zookeeper
#

# 1. create user [username]
# 2. chown [username] permissions to [zooDir]
# 3. install iftop command (to collect machine net traffic info. If not needed, just delete it.)
execute() {
   if [ $# -ne 2 ]; then
      echo "Usage: sh machine_init.sh [username] [zooDir]"
      exit 1;
   fi

   local username="$1"
   local zooDir="$2"
   local userExists=`id $1 | wc -l`
   if [ ${userExists} == 1 ];then
      echo "User $1 exists, overwrite user info: [y/n]?"
      read overwrite
      if [ ${overwrite} == "y" ]; then
         echo "overwrite existed user: $1."
         userdel -r "$1"
         createUser "$1"
      fi
   else
      createUser "$1"
   fi

   if [ -d ${zooDir} ]
      then
      chown -R ${username}:${username} ${zooDir}
      echo "Chown ${username} permissions to ${zooDir}"
   fi

   # install iftop command, to collect machine net traffic info.
   installIftop ${username}
}

# create new user
createUser() {
    echo "Creating user $1"
    useradd -m -d /home/$1 -s /bin/bash $1
    passwd $1
    echo "Create user: $1 successfully."
}

# install iftop
installIftop() {
   if [ -n "$(iftopLocation)" ];then
      echo "iftop already installed"
      setIftop $1 "$(iftopLocation)"
      echo "Setcap for iftop"
   else
      echo "Installing iftop..."
      yum install -y iftop
      echo "iftop is installed"
      setIftop $1 "$(iftopLocation)"
      echo "Setcap for iftop"
   fi
}

# set iftop permission to [username]
setIftop() {
   chgrp $1 $2
   chmod 750 $2
   setcap cap_net_raw,cap_net_admin=ep $2
}

# whether the iftop is present
iftopLocation() {
   echo `which iftop`
}

execute $1 $2;