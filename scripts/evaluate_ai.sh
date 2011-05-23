#!/bin/bash
hg id 
for d1 in decks/LSK_RW.dec decks/LSK_LifeGain.dec decks/LSK_UW.dec decks/LSK_Jund.dec decks/LSK_Skullclamp_Swords.dec; do
for d2 in decks/LSK_RW.dec decks/LSK_LifeGain.dec decks/LSK_UW.dec decks/LSK_Jund.dec decks/LSK_Skullclamp_Swords.dec; do
	java -ea -cp release/Magarena.jar magic.DeckStrCal --games 100 --strength 6 --deck1 $d1 --deck2 $d2 --ai1 $1 --ai2 VEGAS; 
done
done
