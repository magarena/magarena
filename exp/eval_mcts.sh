#!/bin/bash
#$ -t 1-250
#$ -V
#$ -cwd
#$ -e /dev/null
#$ -o /dev/null
#SGE_TASK_ID=100
args=`grep MCTS exp/args | head -$SGE_TASK_ID | tail -1`
java -Xmx512M -DrndSeed=$SGE_TASK_ID -cp build magic.DeckStrCal $args > $SGE_TASK_ID.out 2> $SGE_TASK_ID.err
