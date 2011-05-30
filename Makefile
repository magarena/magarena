JAR=java -ea -cp $^ 
DSC=$(JAR) magic.DeckStrCal
BUILD=build
JOPTS=-Xlint:all -d $(BUILD) -cp $(BUILD):.
SRC=$(shell find -iname *.java) 
#MAG:=release/Magarena-$(shell hg id -n).jar
MAG:=release/Magarena.jar
EXE:=release/Magarena.exe

all: $(MAG) $(EXE) tags

1.14:
	-rm -rf Magarena-1.14
	-rm Magarena-1.14.zip
	mkdir -p Magarena-1.14/Magarena
	cp \
			release/gpl-3.0.html \
			release/Magarena.exe \
			release/Magarena.sh \
			release/Magarena.command \
			release/README.txt \
			Magarena-1.14
	cp -r \
			release/avatars \
			release/decks \
			release/mods \
			release/sounds \
			Magarena-1.14/Magarena
	-zip -r Magarena-1.14.zip Magarena-1.14

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

Test%: 
	java -cp $(MAG) -DtestGame=Test$* magic.MagicMain

$(EXE): $(MAG)
	cd launch4j; ./launch4j ../release/magarena.xml

clean:
	-ant clean
	-rm $(BUILD)/javac.last
	-rm $(MAG)

start: jar
	java -Xmx256M -cp build magic.MagicMain

test: $(MAG)
	$(JAR) -DrndSeed=123 magic.DeckStrCal \
	--deck1 release/decks/LSK_G.dec \
	--ai1 VEGAS \
	--deck2 release/decks/LSK_G.dec \
	--ai2 RND --games 10 --strength 3

exp/%.log: $(MAG)
	scripts/evaluate_ai.sh $* > $@ 
