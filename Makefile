MIN_MEM=256M
MAX_MEM=512M
MAG:=release/Magarena.jar
EXE:=release/Magarena.exe
JAR:=release/Magarena.jar
JAVA=java -Xms$(MIN_MEM) -Xmx$(MAX_MEM) -noverify 
DEBUG=$(JAVA) -ea
LIBS=.:lib/annotations.jar:lib/jsr305.jar:release/lib/groovy-all-*.jar
RUN=$(JAVA) -Dcom.sun.management.jmxremote -cp $(LIBS):$(MAG)
SHELL=/bin/bash
BUILD=build
SRC=$(shell find src -iname "*.java")
NO_OUTPUT:=awk 'BEGIN{RS="\a"}{print; exit 1}'

all: tags $(MAG) $(EXE)

zips:
	make M`grep Release release/README.txt | head -1 | cut -d' ' -f2`

cards_diff: $(MAG)
	for i in `hg stat -q src/magic/card release/Magarena/scripts | cut -d' ' -f2 | sort -t'/' -k4`; do hg diff $$i; done | flip -u - > $@

findbugs_warnings.txt: $(MAG)
	~/App/findbugs/bin/findbugs \
			-textui \
			-progress \
			-sortByClass \
			-emacs \
			-effort:max \
			-output $@ \
			-sourcepath src \
			build

build_warnings.txt:
	make clean all > $@

FILTER_DECKBOX := grep deckbox.org/mtg | grep -o ">[A-Z].*<" | sed 's/^>//;s/<$$//'

cards/standard_all.out:
	touch $@
	$(eval PAGES := $(shell curl http://deckbox.org/games/mtg/cards?f=b31 | grep -o "1 of [0-9]\+" | sed 's/1 of //' | head -1))
	echo processing ${PAGES} pages
	seq ${PAGES} | parallel -q -j 20 -k wget "http://deckbox.org/games/mtg/cards?f=b31&p={}" -O - | ${FILTER_DECKBOX} >> $@

cards/modern_all.out:
	touch $@
	$(eval PAGES := $(shell curl http://deckbox.org/games/mtg/cards?f=b35 | grep -o "1 of [0-9]\+" | sed 's/1 of //' | head -1))
	echo processing ${PAGES} pages
	seq ${PAGES} | parallel -q -j 20 -k wget "http://deckbox.org/games/mtg/cards?f=b35&p={}" -O - | ${FILTER_DECKBOX} >> $@

cards/%_all.txt: cards/%_all.out
	cat $^ | sort | uniq | sed 's/Aether/Æther/' > $@

cards/new.txt: cards/existing_master.txt
	$(eval LAST := $(shell git tag | sort -t. -k 1,1n -k 2,2n -k 3,3n -k 4,4n | tail -1 | cut -d' ' -f1))
	make cards/new_$(LAST).txt
	mv cards/new_$(LAST).txt $@
	make wiki/UpcomingCards.md

cards/fmt.txt: cards/new.txt
	cat $^ | tr " " "@" | tr "\n" "#" | sed 's/#/, /g' | sed 's/, $$//' | fmt | sed 's/@/ /g' > $@

changelog:
	$(eval LAST := $(shell git tag | sort -t. -k 1,1n -k 2,2n -k 3,3n -k 4,4n | tail -1 | cut -d' ' -f1))
	git log ${LAST}..master > changelog

people: changelog
	grep Author: $^ | sed 's/Author:[ ]*//;s/ <.*>//' | sort | uniq | sort > $@

cards/new_%.txt: cards/existing_master.txt cards/existing_%.txt
	join -v1 -t"|" <(sort $(word 1,$^)) <(sort $(word 2,$^)) > $@

cards/new_scripts_%.txt: release/Magarena/scripts
	grep "name=" -h $$(hg diff -r $* | grep -B 1 "^--- /dev/null" | grep $^ | cut -d' ' -f4) | sed 's/name=//' > $@
	flip -u $@

cards/existing_scripts_%.txt: $(wildcard release/Magarena/scripts/*.txt)
	git show $*:release/Magarena/scripts \
	| grep ".txt" \
	| sed 's/^/$*:release\/Magarena\/scripts\//' \
	| git cat-file --batch \
	| grep "name=" \
	| sed 's/.*name=//' \
	| sort > $@
	sed -i 's/\r//' $@

cards/existing_tokens_%.txt: $(wildcard release/Magarena/scripts/*.txt)
	git show $*:release/Magarena/scripts \
	| grep ".txt" \
	| sed 's/^/$*:release\/Magarena\/scripts\//' \
	| git cat-file --batch \
	| awk -f scripts/extract_token_name.awk | sort > $@

cards/existing_code.txt: cards/groovy.txt
	cp cards/groovy.txt $@

cards/existing_%.txt: cards/existing_scripts_%.txt cards/existing_tokens_%.txt
	join -v1 -t"|" <(sort $(word 1,$^)) <(sort $(word 2,$^)) > $@

cards/groovy.txt: $(wildcard release/Magarena/scripts/*.txt)
	grep -ho "name=.*" `grep requires_groovy_code -lr release/Magarena/scripts` | sed 's/name=//' | sort > $@

cards/non-groovy.txt: $(wildcard release/Magarena/scripts/*.txt)
	grep -ho "name=.*" `grep requires_groovy_code -L release/Magarena/scripts/*.txt` | sed 's/name=//' | sort > $@

%_full.txt: scripts/extract_candidates.awk %.txt cards/groovy.txt cards/mtg-data.txt
	awk -f $^ | sed 's/\t/\n/g'  > $@

cards/candidates_full.txt: scripts/extract_candidates.awk cards/scored_by_dec.tsv cards/unimplementable.tsv cards/mtg-data.txt
	awk -f $^ | sort -rg | sed 's/\t/\n/g' > $@

cards/unimplementable.tsv.add: cards/candidates_full.txt
	grep "|" $^ | sed 's/NAME://;s/|/\t/' >> $(basename $@)
	make $^

cards/staples.txt:
	curl \
	https://www.mtggoldfish.com/format-staples/standard/full/creatures \
	https://www.mtggoldfish.com/format-staples/standard/full/lands \
	https://www.mtggoldfish.com/format-staples/standard/full/spells \
	https://www.mtggoldfish.com/format-staples/modern/full/creatures \
	https://www.mtggoldfish.com/format-staples/modern/full/lands \
	https://www.mtggoldfish.com/format-staples/modern/full/spells \
	https://www.mtggoldfish.com/format-staples/pauper/full/creatures \
	https://www.mtggoldfish.com/format-staples/pauper/full/lands \
	https://www.mtggoldfish.com/format-staples/pauper/full/spells \
	https://www.mtggoldfish.com/format-staples/legacy/full/creatures \
	https://www.mtggoldfish.com/format-staples/legacy/full/lands \
	https://www.mtggoldfish.com/format-staples/legacy/full/spells \
	https://www.mtggoldfish.com/format-staples/vintage/full/creatures \
	https://www.mtggoldfish.com/format-staples/vintage/full/lands \
	https://www.mtggoldfish.com/format-staples/vintage/full/spells \
	| pup ".col-card a text{}" | sed "s/&#39;/'/g;s/ (.*)//g" | sort | uniq > $@


cards/unknown.txt:
	grep name= `grep "status=" -Lr release/Magarena/scripts_missing` -h | sed 's/name=//' | sort > $@

cards/unknown_oracle.txt:
	grep oracle= `grep -L status= -r release/Magarena/scripts_missing` | sed 's/oracle=/\n/;s/release/\nrelease/' > $@

cards/staples_unknown.txt: cards/staples.txt cards/unknown.txt
	join -t"|" <(sort $(word 1,$^)) <(sort $(word 2,$^)) > $@

%.out: $(MAG)
	SGE_TASK_ID=$* exp/eval_mcts.sh
	
release/Magarena/themes/felt_theme.zip:
	-mkdir release/Magarena/themes
	wget https://github.com/magarena/magarena-themes/releases/download/1.0/felt_theme.zip -O $@

M1.%: clean $(EXE) release/Magarena/themes/felt_theme.zip
	grep "VERSION.*1.$*" -Ir src/
	grep "Release.*1.$*" release/README.txt
	grep 1.$* -Ir Magarena.app/
	-rm -rf Magarena-1.$*
	-rm -rf Magarena-1.$*.app
	-rm Magarena-1.$*.zip
	-rm Magarena-1.$*.app.zip
	echo "preparing Lin/Win dist"
	mkdir Magarena-1.$*
	cp -r \
			release/gpl-3.0.html \
			release/Magarena.jar \
			release/Magarena.exe \
			release/Magarena.sh \
			release/README.txt \
			release/lib \
			Magarena-1.$*
	mkdir Magarena-1.$*/Magarena
	cp -r \
			release/Magarena/avatars \
			release/Magarena/decks \
			release/Magarena/scripts \
			release/Magarena/scripts_missing \
			release/Magarena/translations \
			Magarena-1.$*/Magarena
	mkdir Magarena-1.$*/Magarena/mods
	cp \
			release/Magarena/mods/*.txt \
			Magarena-1.$*/Magarena/mods
	mkdir Magarena-1.$*/Magarena/themes
	cp \
			release/Magarena/themes/felt_theme.zip \
			Magarena-1.$*/Magarena/themes
	-zip -r Magarena-1.$*.zip Magarena-1.$*
	echo "preparing Mac dist"
	cp -r Magarena.app Magarena-1.$*.app
	cd Magarena-1.$*.app/Contents; ln -s ../../Magarena-1.$* Java
	chmod a+x Magarena-1.$*.app/Contents/MacOS/MagarenaLauncher.sh
	-zip -r Magarena-1.$*.app.zip Magarena-1.$*.app

$(JAR): $(SRC)
	ant -f build.xml

release/Magarena-%.jar:
	make clean $(JAR)
	mv $(JAR) $@

tags: $(SRC)
	ctags -R src

Test%.run: $(MAG)
	$(DEBUG) -DtestGame=Test$* -Dmagarena.dir=`pwd`/release -jar $^ 2>&1 | tee Test$*.log

$(EXE): $(MAG)
	cd launch4j; ./launch4j ../release/magarena.xml

clean:
	-ant clean
	-rm -f $(MAG)

clean/%: Magarena-%.zip Magarena-%.app.zip 
	-rm -rf Magarena-$*
	-rm -rf Magarena-$*.app
	-rm Magarena-$*.zip
	-rm Magarena-$*.app.zip

log.clean:
	-rm -f *.log

inf: $(MAG)
	-while true; do make debug=true 0`date +%s`.t; done

circleci:
	$(eval MAG_ID := $(shell date +%s))
	make clean games=100 ai1=MMABC ai2=MCTS ${MAG_ID}.t || (cat ${MAG_ID}.out && tail -20 ${MAG_ID}.log && false)
	touch cards/standard_all.out cards/modern_all.out
	touch cards/standard_all.txt cards/modern_all.txt
	make zips
	mv release/Magarena.jar *.zip ${CIRCLE_ARTIFACTS}

test-self-play:
	for i in `seq 1 10`; do tsp make games=100 ai1=MMABC ai2=MCTS flags=-ea `date +%N`.t; done

games ?= 10000
str1 ?= 1
str2 ?= 1
life ?= 10
ai1 ?= MMABFast
ai2 ?= MMABFast
debug ?= false
parseMissing ?= false
devMode ?= false
selfMode ?= false
flags ?= 

%.t: $(MAG)
	$(eval VER := $(shell git rev-parse --short HEAD))
	echo ${VER} > $*.log
	$(RUN) ${flags} \
	-Dmagarena.dir=`pwd`/release \
	-Ddebug=${debug} \
	-DparseMissing=${parseMissing} \
	-DdevMode=${devMode} \
	-Dgame.log=$*.log \
	-Djava.awt.headless=true \
	magic.DeckStrCal \
	--seed $* \
	--ai1 ${ai1} --str1 ${str1} \
	--ai2 ${ai2} --str2 ${str2} \
	--life ${life} \
	--games 1 \
	--repeat ${games} > $*.out 2>&1

test: 100.d

debug: $(MAG)
	make 101.t debug=true games=0 flags=-ea || (cat 101.out && false)

%.d: $(MAG)
	$(DEBUG) \
	-DrndSeed=$* \
	-DselfMode=$(selfMode) \
	-Ddebug=${debug} \
	-DdevMode=${devMode} \
	-Dmagarena.dir=`pwd`/release \
	-jar $^ |& tee $*.log

# Z = 4.4172 (99.999%)
# E = 0.01
# best estimator for r is p = h / (h + t)
# this estimator has a margin of error E, |p - r| < E at a particular Z, p - E < r < p + E
# n = Z^2 / 4E^2
#   = 48780
#   ~ 50000
%.str: $(MAG) deck1.dec deck2.dec
	$(RUN) -ea -Dmagarena.dir=`pwd`/release \
	magic.DeckStrCal \
	--ai1 $* \
	--str1 1 \
	--deck1 $(word 2,$^) \
	--ai2 $* \
	--str2 1 \
	--deck2 $(word 3,$^) \
	--games 50000 >> $@ 2>&1 

#exp/%.log: $(MAG)
#	scripts/evaluate_ai.sh $* > $@

decks/dl:
	for i in `curl http://www.wizards.com/magic/magazine/archive.aspx?tag=dailydeck | grep -o mtg/daily/deck/[0-9]* | cut -d'/' -f4`; do make decks/dd_$$i.dec; done
	for i in `curl http://www.wizards.com/magic/magazine/archive.aspx?tag=topdeck | grep -o mtg/daily/td/[0-9]* | cut -d'/' -f4`; do make decks/td_$$i.dec; done
	grep "name=" -r incomplete | sed 's/.*name=/100 /' > decks/with_scripts.dec

decks/with_scripts.dec: $(wildcard incomplete/*.txt)
	cat $^ | grep "name=" | sed 's/.*name=//;s/^/100 /' | sort > $@

%.fix_date:
	touch $* -d "`cat $* | head -2 | tail -1 | sed 's/# //'`"

# Daily Deck
decks/dd_%.dec:
	curl "http://www.wizards.com/Magic/Magazine/Article.aspx?x=mtg/daily/deck/$*" | awk -f scripts/dailyhtml2dec.awk > $@
	make $@.fix_date

# Top Decks
decks/td_%.dec: 
	curl http://www.wizards.com/Magic/Magazine/Article.aspx?x=mtg/daily/td/$* | awk -f scripts/dailyhtml2dec.awk > $@
	make $@.fix_date

# Mike Flores
decks/mf_%.dec:
	curl http://www.wizards.com/Magic/Magazine/Article.aspx?x=mtgcom/daily/mf$* | awk -f scripts/dailyhtml2dec.awk > $@
	make $@.fix_date

# Decks from magic-league.com
decks/ml_%.dec: scripts/apprentice2dec.awk
	wget "http://www.magic-league.com/decks/download.php?deck=$*&index=1" -O - | flip -u - | awk -f $^ > $@

# Decks from www.mtgtop8.com
decks/mtgtop8_%.dec:
	wget "http://www.mtgtop8.com/export_files/deck$*.mwDeck" -O $@

decks/update:
	$(eval LAST := $(shell curl http://mtgtop8.com/search | grep "&d=[0-9]\+" -o | sort | tail -1 | sed 's/&d=//'))
	-seq `expr ${LAST} - 200` ${LAST} | parallel make decks/mtgtop8_{}.dec
	find decks -size 0 -delete

ref/MagicCompRules_latest.txt:
	wget `curl http://magic.wizards.com/en/game-info/gameplay/rules-and-formats/rules | grep -o "http://media.*\.txt"` -O $@

ref/rules.txt: ref/MagicCompRules_latest.txt
	iconv -f UTF-16LE -t UTF-8 $^ | fmt -s > $@
	flip -bu $@

resources/magic/data/icons/missing_card.png:
	convert -background gray -bordercolor black -border 5x5 -size 302x435 \
	-pointsize 30 label:'\nNo card image found\n\nSelect\n\"Download images\"\nfrom Arena menu\n\nOR\n\nSwitch to text mode\nusing the Enter key' $@

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

cards/mtgo_cube.txt:
	wget -O - https://www.wizards.com/magic/magazine/article.aspx?x=mtg/daily/arcana/927 | grep autoCard | sed 's/<[^<]*>//g;s/^[ ]*//g' > $@

cards/mtgo_cube2.txt:
	wget -O - https://www.wizards.com/magic/magazine/article.aspx?x=mtg/daily/other/07032012d | grep autoCard | sed 's/<[^<]*>//g;s/^[ ]*//g' > $@

upload/%: Magarena-%.zip Magarena-%.app.zip 
	make upload/Magarena-$*.app.zip 
	make upload/Magarena-$*.zip

upload/Magarena-%.app.zip: Magarena-%.app.zip 
	scripts/googlecode_upload.py \
			-p magarena \
			-u melvinzhang@gmail.com \
			-w `cat ~/Modules/notes/keys/googlecode_pw.txt` \
			-s "Magarena $* (Mac)" \
			-l Featured,Type-Installer,OpSys-OSX \
			$^

upload/Magarena-%.zip: Magarena-%.zip
	scripts/googlecode_upload.py \
			-p magarena \
			-u melvinzhang@gmail.com \
			-w `cat ~/Modules/notes/keys/googlecode_pw.txt` \
			-s "Magarena $*" \
			-l Featured,Type-Archive,OpSys-Linux,OpSys-Windows \
			$^

%.up: %
	scripts/googlecode_upload.py \
			-p magarena \
			-u melvinzhang@gmail.com \
			-w `cat ~/Modules/notes/keys/googlecode_pw.txt` \
			-s "$^" \
			$^

cards/cards.orig.xml:
	wget `curl 'http://www.slightlymagic.net/forum/viewtopic.php?f=27&t=1347' | grep -o "http[^<]*dl.dropbox[^<]*.zip\"" | sed 's/"//'`
	unzip mtg-data-2015*.zip mtg-data/cards.xml mtg-data/meta.xml mtg-data/setinfo.xml
	mv mtg-data/cards.xml $@
	mv mtg-data/* cards
	rmdir mtg-data
	unzip mtg-data-txt*.zip -d cards

# correct phyrexian mana from {P/.} -> {./P}
cards/cards.xml: cards/cards.orig.xml
	sed "s/{P\/\(.\)}/{\1\/P}/g;s/’/'/g" $^ > $@

cards/scriptable.txt: scripts/analyze_cards.scala scripts/effects.txt cards/cards.xml cards/existing_master.txt
	scala $^ > $@

cards/magicdraftsim-sets:
	curl www.magicdraftsim.com/card-ratings | \
	grep Kamigawa | \
	head -1 | \
	sed 's/value=/\n/g' | \
	sed 's/<.*//' | \
	cut -d\' -f2 | \
	sed '/^$$/d' > $@

cards/magicdraftsim-rating: cards/magicdraftsim-sets
	for i in `cat $^`; do \
	curl http://www.magicdraftsim.com/card-ratings/$$i | \
	pandoc -f html -t plain | \
	grep "^[ ]*[0-9]" | \
	sed "s/^[ ]*[0-9]*/$$i/;s/[ ][ ][ ]*/\t/g"; \
	done > $@

cards/current-magic-excel.txt:
	wget http://www.magictraders.com/pricelists/current-magic-excel.txt -O $@

code_clones:
	java -jar ~/App/simian/bin/simian.jar release/Magarena/scripts/*.groovy > $@

cards/mtg-data:
	curl https://dl.dropbox.com/u/2771470/index.html | grep -o 'href="mtg.*.zip' | head -1 | sed 's/href="//' | xargs -I'{}' wget https://dl.dropbox.com/u/2771470/'{}'
	unzip -j mtg-data*.zip -d cards
	rm mtg-data*.zip

unique_property:
	 grep "=" release/Magarena/scripts/*.txt| cut -d'=' -f1  | sort | uniq -c | sort -n

cards/scored_by_dec.tsv: cards/existing_master.txt cards/unimplementable.tsv $(wildcard decks/*.dec)
	./scripts/score_card.awk decks/*.dec |\
	sort -rg |\
	unaccent iso-8859-15 |\
	./scripts/keep_unimplemented.awk $(word 1,$^) /dev/stdin |\
	./scripts/keep_unimplemented.awk <(cut -f1 $(word 2,$^)) /dev/stdin > $@
	
cards/mtg_mana_costs:
	grep -ho "\(\{[^\}]\+\}\)\+" -r cards/cards.xml | sort | uniq > $@
	
cards/mag_mana_costs:
	grep -ho "\(\{[^\}]\+\}\)\+" -r src/magic/model/MagicManaCost.java release/Magarena/scripts | sort | uniq > $@

cards/mana_cost_graph.dot: cards/mtg_mana_costs
	cat $^ |\
	sed 's/{X}//g;s/{[[:digit:]]*}//g;s/{[CPQT]*}//g;/^$$/d' |\
	awk -f scripts/mana_cost_graph.awk > $@

cards/mana_cost_graph.png: cards/mana_cost_graph.dot
	circo $^ -Tpng -o $@

verify_mana_cost_order: cards/mtg_mana_costs cards/mag_mana_costs
	join -v2 $^

%.update_value: %
	if grep token= $^; then \
		echo "ERROR: Not applicable to tokens"; \
	else \
		name=$$(grep name= $^ | sed 's/name=//');\
		value=$$(curl -sLG http://gatherer.wizards.com/pages/card/details.aspx --data-urlencode "name=$$name" | grep "textRatingValue" | grep -o "[0-9]\.[^<]*" | head -1);\
		if [ ! -z "$$value" ]; then sed -i "s/value=.*/value=$$value/" $^; fi;\
	fi \

%.normalize: %
	flip -u $^
	make $^.update_value
	vim $^
	hg add $^

update_default_value:
	grep value=2.500 -r release/Magarena/scripts release/Magarena/scripts_missing -l | parallel -j 30 make {}.update_value
	git checkout -- `grep "value=$$" -r release/Magarena/scripts release/Magarena/scripts_missing -l`

update_all_value:
	grep value= -r release/Magarena/scripts release/Magarena/scripts_missing -l | parallel -j 30 make {}.update_value
	git checkout -- `grep "value=$$" -r release/Magarena/scripts release/Magarena/scripts_missing -l`

find_event_data: scripts/check_data.awk
	for i in `grep "new MagicEvent(" -lr src`; do \
			grep "new Object\|data\[[0-9\]" $$i > /dev/null && echo $$i; \
			grep "new Object\|data\[[0-9\]" $$i  | awk -f $^ | sed 's/  //g' | sed 's/:/:\t/'; \
	done > $@
	flip -u $@

find_literals:
	grep "\"" src/magic/card/* | awk -f scripts/check_literals.awk

find_single_line_card_code: $(MAG)
	cat src/magic/card/*.java | sed 's/\s\+//g' | sed 's/(.*)/(...)/g' | sort | uniq -c | sort -n | grep publicstaticfinal | grep ");" > $@

find_casts: $(MAG)
	grep -n "([A-Za-z<>]\+)[A-Za-z]\+" -r src/ > $@
	flip -u $@

find_nulls: $(MAG)
	grep -n "null" -r src/ > $@
	flip -u $@

find_ui: $(MAG)
	grep 'magic.ui\|java.awt\|javax.swing' -lr src | grep -v '^src/magic/ui' | sort > $@

find_android: $(MAG)
	find src -iname "*.java" | parallel javac -bootclasspath robovm-rt-1.0.0-beta-03.jar -cp $^ -d build {} 2> $@

# meta check
checks: \
	check_requires_groovy_code \
	check_script_name \
	check_tokens \
	check_unique_property \
	check_required_property \
	check_spells \
	check_groovy_escape \
	check_empty_return \
	check_image \
	check_decks \
	check_mana_or_combat \
	check_color_or_cost \
	check_tap_tap \
	check_no_extra_space

check_property_alignment:
	grep "^[^ ][^=]\+$$" -r release/Magarena/scripts/*.txt  | grep -v requires | grep -v mana_or | grep -v hidden | grep -v overlay | grep -v '#' | ${NO_OUTPUT}

remove_extra_missing:
	git rm `join <(ls -1 release/Magarena/scripts | sort) <(ls -1 release/Magarena/scripts_missing | sort) | sed 's/^/release\/Magarena\/scripts_missing\//'`

remove_extra_groovy:
	git rm `make check_requires_groovy_code | grep '< ' | sed 's/< /release\/Magarena\/scripts\//;s/$$/.groovy/'`

# check rarity using meta.xml
check_rarity: scripts/fix_rarity.scala cards/meta.xml
	cat release/Magarena/scripts/*.txt | scala $^ | grep -v "not be L" | grep -v "Windseeker Centaur" | ${NO_OUTPUT}

# check metadata using cards.xml
check_meta: cards/scriptable.txt
	diff <(cat `grep name= $^ | sed -f scripts/normalize_name.sed | sed 's/name_/release\/Magarena\/scripts\//;s/$$/.txt/'`) \
	     <(sed '/^$$/d' $^) -d  |\
	grep ">" |\
	${NO_OUTPUT}

# every image is to a jpg file or attachment
check_image:
	grep '^image=' -r release/Magarena/scripts | grep -v "jpg$$" | grep -v "png$$" | grep -v attachment.php | ${NO_OUTPUT}

# every card that requires groovy code has a corresponding groovy script file
# every groovy script file has a corresponding card script that requires groovy code
# every groovy script file has a txt file with the same name that requires groovy code
check_requires_groovy_code:
	diff \
	<(ls -1 release/Magarena/scripts/*.groovy | cut -d'/' -f 4 | sed 's/.groovy//' | sort) \
	<(grep requires_groovy_code -r release/Magarena/scripts/ | sed 's/.*=//' | sed 's/;/\n/g' | sed 's/.*scripts\///;s/.txt.*//' | sed -f scripts/normalize_name.sed | sort | uniq)
	grep -L requires_groovy `ls -1 release/Magarena/scripts/*.groovy | sed 's/groovy/txt/'` | ${NO_OUTPUT}

# $ must be escaped as \$ in groovy script
check_groovy_escape:
	grep '[^\\]\$$' -r release/Magarena/scripts | grep -v '\$${' | ${NO_OUTPUT}

# return should not be last token in groovy script
check_empty_return:
	grep "return[[:space:]]*$$" -r release/Magarena/scripts/*.groovy | ${NO_OUTPUT}

# script name is canonical card name
check_script_name:
	diff \
	<(ls -1 release/Magarena/scripts/ | grep txt | sort) \
	<(grep "name=" -r release/Magarena/scripts/ | sort | sed 's/.*name=//' | sed -f scripts/normalize_name.sed | sed 's/$$/.txt/')

# check tokens used match tokens in scripts
check_tokens:
	diff \
	<(grep -ho "name=.*" `grep token= -lr release/Magarena/scripts` | sed 's/name=//' | sort) \
	<(grep 'CardDefinitions.getToken("' -r release/Magarena/scripts src | grep -o '".*"' | sed 's/"//g' | sort | uniq) | grep ">" | ${NO_OUTPUT}

# check that cards in decks exist in the game
check_decks:
	diff \
	<(grep "name=" -r release/Magarena/scripts/ | sed 's/.*name=//' | sort) \
	<(cat release/Magarena/decks/prebuilt/*.dec | grep "^[0-9]" | sed 's/^[0-9]* //' | sort | uniq) | grep ">" | ${NO_OUTPUT}

# each property occurs at most once
check_unique_property:
	grep "^[^=]*" -r release/Magarena/scripts/*.txt | sed 's/=.*//g' | sort | uniq -d | ${NO_OUTPUT}

property_stats:
	grep "^[a-z]*=" -hr release/Magarena/scripts/*.txt -o | sort | uniq -c | sort -n

check_required_property:
	grep ^image= -L release/Magarena/scripts/*.txt | ${NO_OUTPUT}
	grep ^name=  -L release/Magarena/scripts/*.txt | ${NO_OUTPUT}
	grep ^type=  -L release/Magarena/scripts/*.txt | ${NO_OUTPUT}
	grep ^value= -L release/Magarena/scripts/*.txt | ${NO_OUTPUT}
	grep ^rarity= -L `grep token= -L release/Magarena/scripts/*.txt` | ${NO_OUTPUT} 
	grep ^timing= -L `grep token= -L release/Magarena/scripts/*.txt` | ${NO_OUTPUT}

# Instant and Sorcery must have either effect or requires_groovy_code
check_spells:
	 grep requires_groovy_code -L $$(grep effect -L $$(grep "^type.*Instant" -lr release/Magarena/scripts)) | ${NO_OUTPUT}
	 grep requires_groovy_code -L $$(grep effect -L $$(grep "^type.*Sorcery" -lr release/Magarena/scripts)) | ${NO_OUTPUT}

check_unused_condition:
	grep "public static" -r src/magic/model/condition/MagicCondition.java | grep -o "Condition [A-Z]\+[^ =]*" | sed 's/Condition //' | sort | uniq > declared-conds
	for i in `cat declared-conds`; do grep $$i -r src release/Magarena/scripts | grep -v "public static" | grep -o $$i; done | sort | uniq > used-conds
	diff declared-conds used-conds | ${NO_OUTPUT}
	rm declared-conds used-conds

check_unused_filter:
	grep "Impl [A-Z]\+[^ =]*" src/magic/model/target/MagicTargetFilterFactory.java -o | sed 's/Impl //' | sort | uniq > declared-filters
	for i in `cat declared-filters`; do grep $$i -r src release/Magarena/scripts | grep -v "public static final" | grep -o $$i; done | sort | uniq > used-filters
	diff declared-filters used-filters | ${NO_OUTPUT}
	rm declared-filters used-filters

check_unused_choice:
	grep "public static final" -r src/magic/model/choice/MagicTargetChoice.java | grep -o "Choice [A-Z]\+[^ =]*" | sed 's/Choice //' | sort | uniq > declared-choices
	for i in `cat declared-choices`; do grep $$i -r src release/Magarena/scripts | grep -v "public static final" | grep -o $$i; done | sort | uniq > used-choices
	diff declared-choices used-choices | ${NO_OUTPUT}
	rm declared-choices used-choices

check_mana_or_combat:
	diff \
	<(grep mana_or_combat -lr release/Magarena/scripts) \
	<(grep "mana pool.*becomes a" -r release/Magarena/scripts -l)

# all cards except lands should have either color, or cost, or both
check_color_or_cost:
	diff \
	<(grep "^\(cost=\|color=\|type.*Land\)" -r release/Magarena/scripts/*.txt -l) \
	<(ls release/Magarena/scripts/*.txt | sort)

check_tap_tap:
	grep "{T}, Tap" -ir release/Magarena/scripts | grep -v oracle | grep -iv "{T}, Tap another" | ${NO_OUTPUT}

check_no_extra_space:
	grep "^[[:space:]]"  -r resources/magic/data/sets | ${NO_OUTPUT}
	grep "[[:space:]]$$" -r resources/magic/data/sets | ${NO_OUTPUT}

crash.txt: $(wildcard *.log)
	for i in `grep "^Excep" -l $^`; do \
		tail -n +`grep -n "random seed" $$i | tail -1 | cut -d':' -f1` $$i; \
	done >> $@
	rm $^

support/ui:
	for i in src/$@/*.java; do wget https://cakehat.googlecode.com/svn/trunk/$$i -O $$i; done

wiki/UpcomingCards.md: cards/new.txt
	cat <(echo '~~~~') $^ <(echo '~~~~') > $@

parser/test: $(MAG) 
	$(RUN) magic.grammar.Check < grammar/parsable.txt

parser/test_all: $(MAG) grammar/rules.txt
	$(RUN) magic.grammar.Check < $(word 2,$^)

parser/run: $(MAG)
	$(RUN) magic.grammar.Check

grammar/parsable.txt: src/magic/grammar/MagicRuleParser.java
	make parser/test_all > grammar/test_all.out
	cat grammar/test_all.out | grep PARSED | sed 's/PARSED: //' | sort | uniq > $@
	cat grammar/test_all.out | grep FAILED | sort | uniq -c | sort -n > grammar/failed.txt

src/magic/grammar/MagicRuleParser.java: grammar/mtg.peg
	java -cp lib/Mouse-1.6.1.jar mouse.Generate -M -G $^ -P MagicRuleParser -S MagicSyntaxTree -p magic.grammar -r magic.grammar
	mv MagicRuleParser.java $@
	sed -i 's/accept()/sem.action() \&\& accept()/g' $@

grammar/CounterType: grammar/rules.txt
	grep -o "[^ ]* counter \(on\|from\)" $@ | cut -d' ' -f1 | sort | uniq > $@
	# remove a, each, that
	# add poison

cards/cards_per_set.tsv: cards/existing_master_full.txt
	cat <(grep -o ", [A-Z0-9]* [A-Z]" $^ | cut -d' ' -f2) \
	    <(grep -o "^[A-Z0-9]* [A-Z]"  $^ | cut -d' ' -f1) \
	| sort \
	| uniq -c \
	| sort -n \
	| sed 's/^ *//g;s/ /\t/' \
	> $@

smallest.convert:
	make `ls -1S src/magic/card | grep java |sed 's/.java//' | tail -1`.convert
	echo card with code: `ls -1 src/magic/card/*.java | wc -l`
	echo card with script: `ls -1 release/Magarena/scripts/*.groovy | wc -l`

%.convert:
	hg mv src/magic/card/$*.java release/Magarena/scripts/$*.groovy
	vim release/Magarena/scripts/$*.groovy
	sed -i 's/card_/groovy_/' release/Magarena/scripts/$*.txt
	hg commit -m "convert from java code to groovy code"

ai/benchmark.rnd:
	sort -R exp/AIs.txt > exp/rnd.txt
	ts make debug=true games=10 life=20 \
	ai1=`cat exp/rnd.txt | tail -1` \
	str1=`sort -R exp/STRs.txt | tail -1` \
	ai2=`cat exp/rnd.txt | tac | tail -1` \
	str2=`sort -R exp/STRs.txt | tail -1` \
	`date +%s`.t
	ts make ai/benchmark.rnd
	
exp/zermelo.tsv: $(wildcard exp/136*.log)
	awk -f exp/extract_games.awk $^ | ./exp/whr.rb | tac > $@

bytes_per_card.txt:
	for i in `seq 39 68`; do echo -n "1.$$i "; make bytes_per_card.1.$$i -s ; done > bytes_per_card.txt

bytes_per_card.%:
	echo `git show $*:release/Magarena/scripts | tail -n+3 | sed 's/^/$*:release\/Magarena\/scripts\//' | git cat-file --batch | grep -v " blob " | sed 's/^[[:space:]]*//;/^$$/d' | wc -c` \
	/ \
	`git show $*:release/Magarena/scripts | grep '\.txt' | wc -l` \
	| bc -l

reminder.txt: cards/cards.xml
	grep 'reminder="[^"]*"' $^ -o | sed 's/reminder=//' | sort | uniq -c | sort -rn > $@

FILES = release/Magarena/**/*.txt release/Magarena/**/*.groovy release/Magarena/decks/**/*.dec src/**/*.java

normalize_files:
	# add newline at end of file
	shopt -s globstar; sed -i -e '$$a\'        ${FILES}
	# convert DOS newlines to UNIX format
	shopt -s globstar; sed -i -e 's/\x0D$$//'  ${FILES}
	# convert tab to four spaces
	shopt -s globstar; sed -i -e 's/\t/    /g' ${FILES}
	# remove empty lines in scripts
	sed -i -e '/^\s*$$/d' release/Magarena/scripts/*.txt
	# use mtgimage for image
	# make img-mtgimage
	# remove extra scripts_missing

%.post:
	@echo "[img]"`grep -o "http.*jpg" release/Magarena/scripts/$*.txt`"[/img]"
	@echo
	@echo "Magarena/scripts/$*.txt"
	@echo "[code]"
	@cat release/Magarena/scripts/$*.txt
	@echo "[/code]"
	@echo
	@echo "Magarena/scripts/$*.groovy"
	@echo "[code]"
	@cat release/Magarena/scripts/$*.groovy
	@echo "[/code]"

untracked:
	hg stat | sed 's/^. //;s/.*/"&"/' | xargs ls -ltr

common_actions:
	grep -ho "Magic[A-Za-z]*Action" -r release/Magarena/scripts  | sort | uniq -c | sort -n

src/magic/MurmurHash3.java:
	curl https://raw.github.com/infinispan/infinispan/master/commons/src/main/java/org/infinispan/commons/hash/MurmurHash3.java > $@
	patch $@ src/magic/MurmurHash3.diff

img-mtgimage:
	grep mtgimage -L `grep token= -L release/Magarena/scripts/*.txt` | parallel awk -f scripts/set_image.awk {} '>' {.}.img
	-ls -1 release/Magarena/scripts/*.img | parallel mv {} {.}.txt

up:
	cd wiki; git pull
	cd website; git pull

	git fetch -v origin master
	git merge --ff-only origin/master

	git fetch palladiamors

	git fetch -v firemind master
	git log base..firemind/master

firemind:
	git checkout -b temp --no-track firemind/master
	git rebase base --onto master
	git checkout master
	git merge --ff-only temp
	git branch -d temp
	git checkout base
	git merge --ff-only firemind/master
	git checkout master
	git diff origin/master master | sed 's/b\///' >> firemind.diff
	make normalize_files checks debug

%.rebase:
	git checkout -b temp --no-track $*
	git rebase -i master
	git checkout master
	git merge --ff-only temp
	git branch -d temp

properties.diff:
	diff <(cat `grep name= cards/scriptable.txt | sed -f scripts/normalize_name.sed | sed 's/name_/release\/Magarena\/scripts\//;s/$$/.txt/'`) \
	     <(sed '/^$$/d' cards/scriptable.txt) -d -u > $@

common_costs:
	 grep "[^\"]*:" grammar/rules.txt -o | sed 's/>//;s/, /\n/g;s/://' | sort | uniq -c | sort -n | grep -v "{" > $@

push: clean normalize_files checks debug
	git push origin master

resources/magic/data/sets/%.txt:
	curl https://mtgjson.com/json/$*.json | jq -r .cards[].name | LC_ALL=C sort | uniq > $@

missing_override:
	grep public -B1 -r release/Magarena/scripts | awk '/Override/ {skip = NR + 1} NR != skip {print $$0}' | grep -v Override | grep release > $@

parse_new.txt: cards/existing_master.txt
	cp `grep status=not -Lr release/Magarena/scripts_missing/` release/Magarena/scripts
	-rm 101.out
	make parseMissing=true debug
	grep "ERROR " 101.out | sed 's/java.lang.RuntimeException: //' | sed 's/\(ERROR.*\) \(cause: .*\)/\2 \1/' | sort > parse_missing.txt
	grep OK 101.out | sed 's/OK card: //' | sort > parse_ok.txt  
	git clean -qf release/Magarena/scripts
	join -v2 -t'_' $^ parse_ok.txt > $@
	diff parse_new.ignore $@

parse_groovy.txt: cards/groovy.txt
	cp scripts-builder/OUTPUT/scripts_missing/* release/Magarena/scripts
	-rm 101.out
	make parseMissing=true debug
	grep "ERROR " 101.out | sed 's/java.lang.RuntimeException: //' | sed 's/\(ERROR.*\) \(cause: .*\)/\2 \1/' | sort > parse_missing.txt
	grep OK 101.out | sed 's/OK card: //' | sort > parse_ok.txt
	git checkout -- release/Magarena/scripts
	join -t'_' <(sort $^) <(sort parse_ok.txt) > $@
	diff parse_groovy.ignore $@

parse_groovy:
	make cards/groovy.txt
	cd scripts-builder && make gen-groovy
	make parse_groovy.txt

# extract name<tab>image url from gallery page
%.tsv: %.html
	grep "alt.*src.*media" $^ | sed 's/.*alt="\([^"]*\)".*src="\([^"]*\)".*/\1\t\2/' > $@

%.md:
	curl https://code.google.com/p/magarena/wiki/$* \
  | xmllint --html --xpath "//div[@id='wikicontent']" - \
  | pandoc --from html --to markdown --no-wrap \
  > $@

%.add: release/Magarena/scripts_missing/%.txt
	git mv $^ release/Magarena/scripts
	vim release/Magarena/scripts/$*.txt
	$(eval NAME := $(shell grep "name=" $^ | sed 's/name=//'))
	git add release/Magarena/scripts/$*.*
	git commit -m "add ${NAME}"

images.url: missing_images
	cat $^ | parallel -k -q curl -G http://magiccards.info/query --data-urlencode 'q=!{}' | grep -o "http.*.jpg" > $@

model_usage:
	for i in `grep "class [A-Za-z]*" -hro src/magic/model | sed 's/class //'`; do echo -en "$$i\t"; grep $$i -lr src release/Magarena/scripts | wc -l; done > $@

fix_indentation:
	grep "^[ ]*" -r release/Magarena/scripts/*.groovy -o | awk -F':' 'length($$2) < 15 {print $$1 "\t" length($$2) % 4}' | grep -v 0 | cut -f1 | uniq | vim -

fix_trailing_space:
	find release/Magarena/scripts -type f -exec sed --in-place 's/[[:space:]]\+$$//' {} +
	find src -type f -exec sed --in-place 's/[[:space:]]\+$$//' {} +

update_card_property:
	ls -1 replace/* | parallel -q sed -i 's/\([^=]*\)=\(.*\)/s|\1=.*|\1=\2|/;s/\\/\\\\/g'
	ls -1 release/Magarena/scripts/*.txt | parallel -q sed -i -f replace/{/} {}

sims_count:
	grep "sims=[0-9]*" -r 14* -o -h | sed 's/sims=//' | sort -n | histogram.py -x 800 > $@

groovy-by-size:
	ls -1Sr `grep executeEvent release/Magarena/scripts/*.groovy -l` > $@

normalize_scripts.diff:
	diff -d -ru -I rarity= -I removal= -I image -I enchant= -I effect= -I value= -I static= -I timing= -I mana= \
	release/Magarena/scripts scripts-builder/OUTPUT/scripts_missing | grep -v Only > $@

sorted_status.txt:
	grep status= -r release/Magarena/scripts_missing/ -h | sort | uniq > $@
	echo "unknown" `grep -L status= -r release/Magarena/scripts_missing | wc -l` >> $@

# export GITHUB_TOKEN=`cat token`
create-draft-release:
	github-release release \
    --user magarena \
    --repo magarena \
    --tag "${tag}" \
    --name "${name}" \
    --draft

upload-lin-release:
	github-release upload \
    --user magarena \
    --repo magarena \
    --tag "${tag}" \
    --name "Magarena-${tag}.zip" \
    --file Magarena-${tag}.zip

upload-mac-release:
	github-release upload \
    --user magarena \
    --repo magarena \
    --tag "${tag}" \
    --name "Magarena-${tag}.app.zip" \
    --file Magarena-${tag}.app.zip

github-releases.json:
	curl https://api.github.com/repos/magarena/magarena/releases > $@

correct-release-label:
	curl -XPATCH https://api.github.com/repos/magarena/magarena/releases/assets/${mac} -H"Content-Type: application/json" -d'{"name": "Magarena-${tag}.app.zip", "label":"Magarena-${tag}.app.zip for Mac"}' -u ${username}
	curl -XPATCH https://api.github.com/repos/magarena/magarena/releases/assets/${linux} -H"Content-Type: application/json" -d'{"name": "Magarena-${tag}.zip", "label":"Magarena-${tag}.zip for Linux/Windows"}' -u ${username}

%_img.tsv:
	paste <(cat image-gallery | pup 'img attr{alt}') <(cat image-gallery | pup 'img attr{src}') > $@
	# edit tsv file to use only ASCII characters

%_fix_image:
	grep /$*/ -r release/Magarena/scripts_missing release/Magarena/scripts -l | parallel awk -f scripts/update_image.awk $*_img.tsv {} '>' {}.new
	grep /$*/ -r release/Magarena/scripts_missing/*.txt release/Magarena/scripts/*.txt  -l | parallel mv {}.new {}

# Match abilities in scripts_missing to existing groovy code
#   assume scripts-builder/OUTPUT/scripts_missing contains card script for current cards
#   groovy scripted effect/ability are those that are in scripts-builder generated card script but not present in actual card script
#   search for these groovy effect/ability in release/Magarena/scripts_missing
groovy_scripted.txt:
	join -v2 -t'_' \
	<(grep "effect=\|ability=\|^ "  -r release/Magarena/scripts/*.txt -h         | sed 's/^ *//;s/.*=//;s/;\\//' | sort | uniq) \
	<(grep "effect=\|ability=\|^ "  -r scripts-builder/OUTPUT/scripts_missing -h | sed 's/^ *//;s/.*=//;s/;\\//' | sort | uniq) \
	> $@

requires_scripted.txt: groovy_scripted.txt
	join -t'_' \
	$^ \
	<(grep "effect=\|ability=\|^ " -r release/Magarena/scripts_missing -h        | sed 's/^ *//;s/.*=//;s/;\\//' | sort | uniq) \
	> $@

requires_scripted_candidate.txt: requires_scripted.txt
	cat $^ | parallel -k grep '{}' -r release/Magarena/scripts_missing > $@

requires_scripted_candidate: cards/existing_master.txt
	cd scripts-builder && make gen-groovy-all
	rm groovy_scripted.txt requires_scripted.txt requires_scripted_candidate.txt
	make requires_scripted_candidate.txt
	diff requires_scripted_candidate.ignore requires_scripted_candidate.txt
