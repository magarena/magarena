#!/bin/bash
#$ -t 1-1000
#$ -V
#$ -cwd
#$ -e /dev/null
#$ -o /dev/null
#SGE_TASK_ID=100
args=`head -$SGE_TASK_ID ~/magarena/exp/args | tail -1`
java -Xmx256M -DrndSeed=$SGE_TASK_ID -cp ~/magarena/build magic.DeckStrCal $args > $SGE_TASK_ID.out 2> $SGE_TASK_ID.err
