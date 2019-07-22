#!/bin/sh

cd $1
cmd/rnn-tagger-french.sh tmp.txt > tmp_tagged.txt
echo "DONE"