for i in `cat decks`; do 
for j in `cat AIs`; do 
for k in `cat decks`; do 
for l in `cat games`; do 
echo --deck1 $i --ai1 $j --deck2 $k --ai2 VEGAS --games $l; 
done 
done 
done
done
