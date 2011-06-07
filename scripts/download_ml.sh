#!/bin/zsh
for i in `seq 10592 -1 10590`; do
    make ml_$i.dec
    sleep 1
done
