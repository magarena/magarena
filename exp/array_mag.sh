#!/bin/bash
#$ -t 1-1000
#$ -V
#$ -cwd
#$ -e /dev/null
#$ -o /dev/null
SGE_TASK_ID=100
pep=`head -$SGE_TASK_ID ~/magarena/exp/args | tail -1`
java -Xmx1G -cp ~/magarena/build magic.DeckStrCal $pep > $SGE_TASK_ID.out 2> $SGE_TASK_ID.err
