#!/bin/bash
hg id 
for d1 in release/decks/LSK_RW.dec release/decks/LSK_LifeGain.dec release/decks/LSK_UW.dec release/decks/LSK_Jund.dec release/decks/LSK_Skullclamp_Swords.dec; do
for d2 in release/decks/LSK_RW.dec release/decks/LSK_LifeGain.dec release/decks/LSK_UW.dec release/decks/LSK_Jund.dec release/decks/LSK_Skullclamp_Swords.dec; do
	java -Xmx1G -cp release/Magarena.jar magic.DeckStrCal --games 100 --strength 6 --deck1 $d1 --deck2 $d2 --ai1 $1 --ai2 VEGAS; 
done
done
