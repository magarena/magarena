DSC=java -ea -cp $^ magic.DeckStrCal
MAG:=release/Magarena-$(shell hg id -n).jar

tags: $(MAG) 
	ctags -R .

$(MAG): 
	-ant
	-cp release/Magarena.jar $(MAG)

Magarena.exe: $(MAG)
	cd launch4j; ./launch4j ../release/arena.xml

clean:
	ant clean

test: $(MAG)
	$(DSC) \
	--deck1 decks/LSK_G.dec \
	--ai1 MCTSD \
	--deck2 decks/LSK_G.dec \
	--ai2 RND --games 10 --strength 3

exp/vegas_mcts_%.log: $(MAG)
	$(DSC) --deck1 decks/LSK_G.dec --deck2 decks/LSK_G.dec --ai1 VEGAS --ai2 MCTS --games $* > $@

exp/mmab_mcts_%.log: $(MAG)
	$(DSC) --deck1 decks/LSK_G.dec --deck2 decks/LSK_G.dec --ai1 MMAB --ai2 MCTS --games $* > $@

exp/rnd_mcts_%.log: $(MAG)
	$(DSC) --deck1 decks/LSK_G.dec --deck2 decks/LSK_G.dec --ai1 RND --ai2 MCTS --games $* > $@

exp/rnd_rnd_%.log: $(MAG)
	$(DSC) --deck1 decks/text.dec --deck2 decks/text.dec --ai1 RND --ai2 RND --games $* > $@

exp/rnd_mmab_%.log: $(MAG)
	$(DSC) --deck1 decks/text.dec --deck2 decks/text.dec --ai1 RND --ai2 MMAB --games $* > $@

exp/mmab_mmab_%.log: $(MAG)
	$(DSC) --deck1 decks/text.dec --deck2 decks/text.dec --ai1 MMAB --ai2 MMAB --games $* > $@

exp/mcts_mcts_%.log: $(MAG)
	$(DSC) --deck1 decks/text.dec --deck2 decks/text.dec --ai1 MCTS --ai2 MCTS --games $* > $@
