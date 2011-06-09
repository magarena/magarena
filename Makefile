JAR=java -ea -cp $^ 
SHELL=/bin/bash
DSC=$(JAR) magic.DeckStrCal
BUILD=build
JOPTS=-Xlint:all -d $(BUILD) -cp $(BUILD):.
SRC=$(shell find -iname *.java) 
#MAG:=release/Magarena-$(shell hg id -n).jar
MAG:=release/Magarena.jar
EXE:=release/Magarena.exe

all: $(MAG) $(EXE) tags

release/mods/extended_cube.txt: existing.txt extended_all.txt
	join -t"|" <(sort $(word 1,$^)) <(sort $(word 2,$^)) > $@

release/mods/standard_cube.txt: existing.txt standard_all.txt
	join -t"|" <(sort $(word 1,$^)) <(sort $(word 2,$^)) > $@

extended_all.txt:
	curl "http://magiccards.info/query?q=f%3Aextended&s=cname&v=olist&p=1" | grep "en/" | sed 's/<[^>]*>//g' > $@
	curl "http://magiccards.info/query?q=f%3Aextended&s=cname&v=olist&p=2" | grep "en/" | sed 's/<[^>]*>//g' >> $@
	curl "http://magiccards.info/query?q=f%3Aextended&s=cname&v=olist&p=3" | grep "en/" | sed 's/<[^>]*>//g' >> $@

standard_all.txt:
	curl "http://magiccards.info/query?q=f%3Astandard&s=cname&v=olist&p=1" | grep "en/" | sed 's/<[^>]*>//g' > $@
	curl "http://magiccards.info/query?q=f%3Astandard&s=cname&v=olist&p=2" | grep "en/" | sed 's/<[^>]*>//g' >> $@

existing.txt: resources/magic/data/cards.txt resources/magic/data/cards2.txt
	cat $^ | grep "^>" | sed 's/>//' | sort > $@

existing_full.txt: newcards/existing.txt data/mtg-data.txt
	awk -f scripts/extract_existing.awk $^ > $@

candidate_cards_full.txt: scripts/extract_candidates.awk candidate_cards.tsv data/mtg-data.txt
	awk -f $^ | sort -rg | sed 's/\t/\n/g' > $@

%.out: $(MAG)
	SGE_TASK_ID=$* exp/array_mag.sh

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

jar: $(MAG)
	java -Xmx256M -jar $^

test: $(MAG)
	$(JAR) -DrndSeed=123 magic.DeckStrCal \
	--deck1 release/decks/LSK_G.dec \
	--ai1 VEGAS \
	--deck2 release/decks/LSK_G.dec \
	--ai2 RND --games 10 --strength 3

exp/%.log: $(MAG)
	scripts/evaluate_ai.sh $* > $@ 

decks/dd_%.dec: scripts/dailyhtml2dec.awk
	curl "http://www.wizards.com/Magic/Magazine/Article.aspx?x=mtg/daily/deck/$*" | awk -f $^ > $@

decks/ml_%.dec: scripts/apprentice2dec.awk 
	wget "http://www.magic-league.com/decks/download.php?deck=$*&index=1" -O - | flip -u - | awk -f $^ > $@
