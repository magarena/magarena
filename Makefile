JAR=java -ea -cp $^ 
DSC=$(JAR) magic.DeckStrCal
BUILD=build
JOPTS=-Xlint:all -d $(BUILD) -cp $(BUILD):.
SRC=$(shell find -iname *.java) 
#MAG:=release/Magarena-$(shell hg id -n).jar
MAG:=release/Magarena.jar
EXE:=release/Magarena.exe

all: $(MAG) $(EXE) tags

newcards/existing.txt: resources/magic/data/cards.txt resources/magic/data/cards2.txt
	cat $^ | grep "^>" | sed 's/>//' | sort > $@

newcards/existing_full.txt: newcards/existing.txt data/mtg-data.txt
	awk -f scripts/extract_existing.awk $^ > $@

M1.%:
	-rm -rf Magarena-1.$*
	-rm Magarena-1.$*.zip
	mkdir -p Magarena-1.$*/Magarena/mods
	cp \
			release/gpl-3.0.html \
			release/Magarena.exe \
			release/Magarena.sh \
			release/Magarena.command \
			release/README.txt \
			Magarena-1.$*
	cp -r \
			release/avatars \
			release/decks \
			release/sounds \
			Magarena-1.$*/Magarena
	cp \
			release/mods/felt_theme.zip \
			Magarena-1.$*/Magarena/mods
	-zip -r Magarena-1.$*.zip Magarena-1.$*

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

.Test%: $(MAG)
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

%.dec: scripts/dailyhtml2dec.awk
	curl http://www.wizards.com/Magic/Magazine/Article.aspx?x=mtg/daily/deck/$* | awk -f $^ > $@
