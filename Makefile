DSC=java -ea -cp $^ magic.DeckStrCal
BUILD=build
JOPTS=-Xlint:all -d $(BUILD) -cp $(BUILD):.
SRC=$(shell find -iname *.java) 
#MAG:=release/Magarena-$(shell hg id -n).jar
MAG:=release/Magarena.jar
EXE:=release/Magarena.exe

all: $(MAG) $(EXE) tags

jar: $(MAG)

exe: $(EXE)

$(MAG): $(SRC) 
	ant

$(BUILD)/javac.last: $(SRC)
	-mkdir $(BUILD)
	javac $(JOPTS) $?
	cp -r resources/* $(BUILD)
	touch $@

tags: $(SRC) 
	ctags -R .

$(EXE): $(MAG)
	cd launch4j; ./launch4j ../release/magarena.xml

clean:
	-ant clean
	-rm $(BUILD)/javac.last

test: $(MAG)
	$(DSC) \
	--deck1 release/decks/LSK_G.dec \
	--ai1 VEGAS \
	--deck2 release/decks/LSK_G.dec \
	--ai2 RND --games 10 --strength 3

exp/%.log: $(MAG)
	scripts/evaluate_ai.sh $* > $@ 
