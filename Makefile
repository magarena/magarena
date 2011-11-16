JAVAEA=java -ea -Xms256M -Xmx256M -Ddebug=true 
LIBS=.:lib/annotations.jar:lib/jsr305.jar
JAVA=java -Xms256M -Xmx256M -Ddebug=true -Dcom.sun.management.jmxremote -cp $(LIBS):release/Magarena.jar 
SHELL=/bin/bash
BUILD=build
JOPTS=-Xlint:all -d $(BUILD) -cp $(LIBS):$(BUILD):.
SRC=$(shell find src -iname *.java) 
MAG:=release/Magarena.jar
EXE:=release/Magarena.exe

all: $(MAG) $(EXE) tags

check_literals:
	grep "\"" src/magic/card/* | awk -f scripts/check_literals.awk

cubes: \
	release/mods/legacy_cube.txt \
	release/mods/extended_cube.txt \
	release/mods/standard_cube.txt \
	release/mods/modern_cube.txt

themes: \
	release/mods/felt_theme.zip \
	release/mods/blackswamp_theme.zip \
	release/mods/bluemarble_theme.zip \
	release/mods/darkbattle_theme.zip \
	release/mods/gothic_theme.zip \
	release/mods/greenforest_theme.zip \
	release/mods/moon_theme.zip \
	release/mods/mystic_theme.zip \
	release/mods/nature_theme.zip \
	release/mods/redfire_theme.zip \
	release/mods/whiteangel_theme.zip

cards_diff: $(MAG)
	for i in `hg stat -q src/magic/card release/Magarena/scripts | cut -d' ' -f2 | sort -t'/' -k4`; do hg diff $$i; done | flip -u - > $@

code_to_remove: $(MAG)
	cat src/magic/card/*.java | sed 's/\s\+//g' | sort | uniq -c | sort -n | grep publicstaticfinal | grep ");" > $@

casts: $(MAG) 
	grep -n "([A-Z]\+[a-z]\+[A-Za-z]*)" -r src/ | flip -u > $@

warnings_H.txt: warnings.txt
	grep "(H)" $^ | grep -v System.out | grep -v System.err | grep -v EXS > $@

warnings.txt: $(MAG)
	~/App/findbugs-1.3.9/bin/findbugs \
			-textui \
			-progress \
			-sortByClass \
			-emacs \
			-effort:max \
			-output $@ \
			-sourcepath src \
			build

release/mods/legacy_cube.txt: cards/existing.txt cards/legacy_banned.txt
	join -v1 -t"|" <(sort $(word 1,$^)) <(sort $(word 2,$^)) > $@

release/mods/%_cube.txt: cards/existing.txt cards/%_all.txt
	join -t"|" <(sort $(word 1,$^)) <(sort $(word 2,$^)) > $@

cards/modern_all.txt:
	curl "http://magiccards.info/query?q=cmc%3D0+f%3Amodern&s=cname&v=olist" | grep "en/" | sed 's/<[^>]*>//g' > $@
	curl "http://magiccards.info/query?q=cmc%3D1+f%3Amodern&s=cname&v=olist" | grep "en/" | sed 's/<[^>]*>//g' >> $@
	curl "http://magiccards.info/query?q=cmc%3D2+f%3Amodern&s=cname&v=olist&p=1" | grep "en/" | sed 's/<[^>]*>//g' >> $@
	curl "http://magiccards.info/query?q=cmc%3D2+f%3Amodern&s=cname&v=olist&p=2" | grep "en/" | sed 's/<[^>]*>//g' >> $@
	curl "http://magiccards.info/query?q=cmc%3D3+f%3Amodern&s=cname&v=olist&p=1" | grep "en/" | sed 's/<[^>]*>//g' >> $@
	curl "http://magiccards.info/query?q=cmc%3D3+f%3Amodern&s=cname&v=olist&p=2" | grep "en/" | sed 's/<[^>]*>//g' >> $@
	curl "http://magiccards.info/query?q=cmc%3D4+f%3Amodern&s=cname&v=olist&p=1" | grep "en/" | sed 's/<[^>]*>//g' >> $@
	curl "http://magiccards.info/query?q=cmc%3D4+f%3Amodern&s=cname&v=olist&p=2" | grep "en/" | sed 's/<[^>]*>//g' >> $@
	curl "http://magiccards.info/query?q=cmc%3D5+f%3Amodern&s=cname&v=olist" | grep "en/" | sed 's/<[^>]*>//g' >> $@
	curl "http://magiccards.info/query?q=cmc>%3D6+f%3Amodern&s=cname&v=olist" | grep "en/" | sed 's/<[^>]*>//g' >> $@

cards/extended_all.txt:
	curl "http://magiccards.info/query?q=cmc%3D0+f%3Aextended&s=cname&v=olist" | grep "en/" | sed 's/<[^>]*>//g' > $@
	curl "http://magiccards.info/query?q=cmc%3D1+f%3Aextended&s=cname&v=olist" | grep "en/" | sed 's/<[^>]*>//g' >> $@
	curl "http://magiccards.info/query?q=cmc%3D2+f%3Aextended&s=cname&v=olist" | grep "en/" | sed 's/<[^>]*>//g' >> $@
	curl "http://magiccards.info/query?q=cmc%3D3+f%3Aextended&s=cname&v=olist" | grep "en/" | sed 's/<[^>]*>//g' >> $@
	curl "http://magiccards.info/query?q=cmc%3D4+f%3Aextended&s=cname&v=olist" | grep "en/" | sed 's/<[^>]*>//g' >> $@
	curl "http://magiccards.info/query?q=cmc%3D5+f%3Aextended&s=cname&v=olist" | grep "en/" | sed 's/<[^>]*>//g' >> $@
	curl "http://magiccards.info/query?q=cmc>%3D6+f%3Aextended&s=cname&v=olist" | grep "en/" | sed 's/<[^>]*>//g' >> $@

cards/standard_all.txt:
	curl "http://magiccards.info/query?q=f%3Astandard&s=cname&v=olist&p=1" | grep "en/" | sed 's/<[^>]*>//g' > $@
	curl "http://magiccards.info/query?q=f%3Astandard&s=cname&v=olist&p=2" | grep "en/" | sed 's/<[^>]*>//g' >> $@

cards/new.txt: cards/existing.txt
	diff $^ cards/existing_old.txt | grep "<" | sed 's/< /  /' > $@

cards/existing.txt: $(wildcard release/Magarena/scripts/*.txt)
	cat $^ | grep "^>" | sed 's/>//' | sort > $@
	flip -u $@

cards/existing_full.txt: cards/existing.txt cards/mtg-data.txt
	awk -f scripts/extract_existing.awk $^ > $@

cards/candidates_full.txt: scripts/extract_candidates.awk cards/candidates.txt cards/mtg-data.txt
	awk -f $^ | sort -rg | sed 's/\t/\n/g' > $@

%.out: $(MAG)
	SGE_TASK_ID=$* exp/eval_mcts.sh

M1.%: clean all cubes
	grep "VERSION.*1.$*" -r src/*
	grep "Release.*1.$*" release/README.txt
	-rm -rf Magarena-1.$*
	-rm -rf Magarena-1.$*.app
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
			release/mods/*.txt \
			Magarena-1.$*/Magarena/mods
	-zip -r Magarena-1.$*.zip Magarena-1.$*
	cp -r Magarena.app Magarena-1.$*.app
	cd Magarena-1.$*.app/Contents/Resources; ln -s ../../../Magarena-1.$* Java
	chmod a+x Magarena-1.$*.app/Contents/MacOS/JavaApplicationStub
	-zip -r Magarena-1.$*.app.zip Magarena-1.$*.app 

$(MAG): $(SRC) 
	ant -f build-safe.xml

class: $(BUILD)/javac.last

$(BUILD)/javac.last: $(SRC)
	-mkdir $(BUILD)
	javac $(JOPTS) $?
	cp -r resources/* $(BUILD)
	touch $@

tags: $(SRC) 
	ctags -R .

.Test%: $(MAG)
	$(JAVA) -DtestGame=Test$* -jar $^

$(EXE): $(MAG)
	cd launch4j; ./launch4j ../release/magarena.xml

clean:
	-ant clean
	-rm $(BUILD)/javac.last
	-rm $(MAG)

jar: $(MAG)
	$(JAVA) -jar $^

%.g: $(MAG)
	$(JAVA) -DrndSeed=$* magic.MagicMain |& tee $*.log

inf: $(MAG)
	-while true; do make `date +%H%M%S`.t; done

test: $(MAG)
	-make `date +%H%M%S`.g

%.speed: $(MAg)
	$(JAVA) magic.DeckStrCal --deck1 release/decks/LSK_B.dec --deck2 release/decks/LSK_G.dec --ai1 $* --ai2 $* --games 100

%.t: $(MAG)
	echo `hg id -n` > $*.log
	$(JAVA) -DrndSeed=$* -DselfMode magic.MagicMain >> $*.log 2>&1

%.d: $(MAG)
	$(JAVAEA) -DrndSeed=$* -jar $^ |& tee $*.log

exp/%.log: $(MAG)
	scripts/evaluate_ai.sh $* > $@ 

decks/dd_%.dec: scripts/dailyhtml2dec.awk
	curl "http://www.wizards.com/Magic/Magazine/Article.aspx?x=mtg/daily/deck/$*" | awk -f $^ > $@

decks/ml_%.dec: scripts/apprentice2dec.awk 
	wget "http://www.magic-league.com/decks/download.php?deck=$*&index=1" -O - | flip -u - | awk -f $^ > $@

# Mike Flores 1 - 212
decks/mf_%.dec: scripts/dailyhtml2dec.awk
	curl http://www.wizards.com/Magic/Magazine/Article.aspx?x=mtgcom/daily/mf$* | awk -f $^ > $@

# Top Decks 1 - 147
decks/td_%.dec: scripts/dailyhtml2dec.awk
	curl http://www.wizards.com/Magic/Magazine/Article.aspx?x=mtg/daily/td/$* | awk -f $^ > $@

ref/rules.txt:
	curl http://www.wizards.com/magic/comprules/MagicCompRules_20110930.txt | fmt -s > $@
	flip -u $@

resources/magic/data/icons/missing_card.png:
	convert -background gray -bordercolor black -border 5x5 -size 302x435 \
	-pointsize 30 label:'\nNo card image found\n\nSelect\n\"Download images\"\nfrom Arena menu\n\nOR\n\nSwitch to text mode\nusing the Enter key' $@

release/mods/%_theme.zip: release/mods/%_theme
	 zip -j $@ $^/*

cards/evan_cube.txt:
	curl http://www.cubedrafting.com/view-the-cube/ | grep jTip | sed "s/<[^>]*>//g;s/\&\#8217;/'/" > $@

cards/brett_cube.txt:
	curl http://www.snazzorama.com/magic/cube/ | grep ":WizardsAutoCard" | sed "s/<\/td>.*//;s/<[^>]*>//g;s/\&\#8217;/'/" > $@

cards/tom_cube.txt:
	wget -O - http://www.tomlapille.com/cube/tom_list.html | sed 's/<[^>]*>//g;s/^[ ]*//g;/^$$/d' > $@

cards/adam_cube.txt:
	wget -O - http://www.tomlapille.com/cube/adam_list.html | sed 's/<[^>]*>//g;s/^[ ]*//g;/^$$/d' > $@

cards/AWinnarIsYou_cube.txt:
	wget -O - http://www.tomlapille.com/cube/winnar_list.html | sed 's/<[^>]*>//g;s/^[ ]*//g;/^$$/d' > $@

daily: $(EXE)
	mv $^ Magarena_`hg id -n`.exe
	scripts/googlecode_upload.py \
			-s "build `hg id -n`" \
			-p magarena \
			-u melvinzhang@gmail.com \
			-w `cat ~/Modules/notes/keys/googlecode_pw.txt` \
			Magarena_`hg id -n`.exe

%.up: %
	scripts/googlecode_upload.py \
			-s "$^" \
			-p magarena \
			-u melvinzhang@gmail.com \
			-w `cat ~/Modules/notes/keys/googlecode_pw.txt` \
			$^

cards/scriptable.txt: scripts/analyze_cards.scala scripts/effects.txt cards/cards.xml
	scala $^ > $@ 2> cards/others.txt

cards/magicdraftsim-rating: cards/card-ratings
	for i in `cat $^`; do \
	curl http://www.magicdraftsim.com/card-ratings/$$i | \
	html2text | \
	grep "^[0-9]" | \
	sed "s/^[0-9]*/$$i/"; \
	done > $@

cards/current-magic-excel.txt:
	wget http://www.magictraders.com/pricelists/current-magic-excel.txt -O $@

pull-beholder:
	hg pull https://code.google.com/r/beholder1-magarena/
