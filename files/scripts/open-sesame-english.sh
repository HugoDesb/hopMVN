#!/bin/sh

# $1 = open sesame folder
# $2 = target-id model name
# $3 = frame-id model name
# $4 = arg-id model name


cd $1

python2.7 -m sesame.targetid --mode=predict --model $2 --raw_input=sentences.txt && python2.7 -m sesame.frameid --mode predict --model $3 --raw_input=logs/$2/predicted-targets.conll && python2.7 -m sesame.argid --mode predict --model $4 --raw_input=logs/$3/predicted-frames.conll
echo "DONE"