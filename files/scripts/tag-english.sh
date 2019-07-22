#!/bin/sh

cd $1
cmd/rnn-tagger-english.sh tmp.txt > tmp_tagged.txt
echo "DONE"