DSC=java -ea -cp $^ magic.DeckStrCal
SRC=$(shell find -iname *.java) 
#MAG:=release/Magarena-$(shell hg id -n).jar
MAG:=release/Magarena.jar
EXE:=release/Magarena.exe

all: $(MAG) $(EXE) tags

jar: $(MAG)

exe: $(EXE)

$(MAG): $(SRC) 
	-ant
#	-cp release/Magarena.jar $(MAG)

tags: $(SRC) 
	ctags -R .

$(EXE): $(MAG)
	cd launch4j; ./launch4j ../release/arena.xml

clean:
	ant clean

test: $(MAG)
	$(DSC) \
	--deck1 decks/LSK_G.dec \
	--ai1 MCTSD \
	--deck2 decks/LSK_G.dec \
	--ai2 RND --games 10 --strength 3

exp/%.log: $(MAG)
	scripts/evaluate_ai.sh $* > $@ 
