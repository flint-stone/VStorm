#!/bin/bash

NUM_OF_SERVERS=10
NUM_OF_CLIENTS=1

echo "1:127.0.0.1:5000:5000:30" > resource/c_stat

COUNTER=1
#dest="resource/${COUNTER}_c_servertarget"
echo $dest
echo hello
while [ $COUNTER -le $NUM_OF_CLIENTS ]
do
	echo hello
	echo "$(((RANDOM % $NUM_OF_SERVERS)+1)):127.0.0.1:12345" > resource/${COUNTER}_c_servertarget
	let COUNTER=COUNTER+1
done

COUNTER=1
while [ $COUNTER -le $NUM_OF_SERVERS ]
do
	echo $(((RANDOM % 30)+5000)) > resource/${COUNTER}_s_resource
	let COUNTER=COUNTER+1
done
