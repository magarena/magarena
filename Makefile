JAVAEA=java -ea -Xms256M -Xmx256M -Ddebug=true
LIBS=.:lib/annotations.jar:lib/jsr305.jar:release/lib/groovy-all-2.1.1.jar
JAVA=${JAVAEA} -Dcom.sun.management.jmxremote -cp $(LIBS):release/Magarena.jar
SHELL=/bin/bash
BUILD=build
SRC=$(shell find src -iname *.java)
MAG:=release/Magarena.jar
EXE:=release/Magarena.exe

all: tags $(MAG) $(EXE)

zips:
	make M`grep Release release/README.txt | head -1 | cut -d' ' -f2`

cubes: \
	cards/standard_all.txt \
	cards/extended_all.txt \
	cards/modern_all.txt \
	release/Magarena/mods/legacy_cube.txt \
	release/Magarena/mods/extended_cube.txt \
	release/Magarena/mods/standard_cube.txt \
	release/Magarena/mods/modern_cube.txt

themes: \
	release/Magarena/mods/felt_theme.zip \
	release/Magarena/mods/brown_theme.zip \
	release/Magarena/mods/blackswamp_theme.zip \
	release/Magarena/mods/bluemarble_theme.zip \
	release/Magarena/mods/darkbattle_theme.zip \
	release/Magarena/mods/gothic_theme.zip \
	release/Magarena/mods/greenforest_theme.zip \
	release/Magarena/mods/moon_theme.zip \
	release/Magarena/mods/mystic_theme.zip \
	release/Magarena/mods/nature_theme.zip \
	release/Magarena/mods/redfire_theme.zip \
	release/Magarena/mods/whiteangel_theme.zip

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

cards/legacy_banned.txt:
	curl https://www.wizards.com/Magic/TCG/Resources.aspx?x=judge/resources/sfrlegacy | grep nodec | grep -o ">[^<]*</a" | sed 's/>//g;s/<\/a//;' > $@

release/Magarena/mods/legacy_cube.txt: cards/existing_tip.txt cards/legacy_banned.txt
	join -v1 -t"|" <(sort $(word 1,$^)) <(sort $(word 2,$^)) > $@

release/Magarena/mods/%_cube.txt: cards/existing_tip.txt cards/%_all.txt
	join -t"|" <(sort $(word 1,$^)) <(sort $(word 2,$^)) > $@

cards/%_all.out:
	touch $@
	for rarity in mythic rare uncommon common land special; do \
		curl --compressed "http://magiccards.info/query?q=r%3A$$rarity+f%3A$*&s=cname&v=olist&p=1" | grep "en/" >> $@; \
		curl --compressed "http://magiccards.info/query?q=r%3A$$rarity+f%3A$*&s=cname&v=olist&p=2" | grep "en/" >> $@; \
		curl --compressed "http://magiccards.info/query?q=r%3A$$rarity+f%3A$*&s=cname&v=olist&p=3" | grep "en/" >> $@; \
	done
	sed -i 's/<[^>]*>//g;s/^[ ]*//g' $@
	sed -i 's/Æ/AE/' $@

cards/%_all.txt: cards/%_all.out
	sort $^ | uniq > $@

cards/new.txt: cards/existing_tip.txt
	$(eval LAST := $(shell hg tags | grep "^[[:digit:]]" | head -1 | cut -d' ' -f1))
	make cards/new_$(LAST).txt
	mv cards/new_$(LAST).txt $@
	make wiki/UpcomingCards.wiki

cards/new_%.txt: cards/existing_tip.txt cards/existing_%.txt
	join -v1 -t"|" <(sort $(word 1,$^)) <(sort $(word 2,$^)) > $@

cards/new_scripts_%.txt: release/Magarena/scripts
	grep "name=" -h $$(hg diff -r $* | grep -B 1 "^--- /dev/null" | grep $^ | cut -d' ' -f4) | sed 's/name=//' > $@
	flip -u $@

cards/existing_scripts_%.txt: $(wildcard release/Magarena/scripts/*.txt)
	hg cat -r $* release/Magarena/scripts | grep "name=" | sed 's/.*name=//' | sort > $@
	sed -i 's/\r//' $@

cards/existing_tokens_%.txt: $(wildcard release/Magarena/scripts/*.txt)
	hg cat -r $* release/Magarena/scripts | grep -C 1 "token=" | grep "name=" | sed 's/.*name=//' | sort > $@

cards/existing_%.txt: cards/existing_scripts_%.txt cards/existing_tokens_%.txt
	join -v1 -t"|" <(sort $(word 1,$^)) <(sort $(word 2,$^)) > $@

%_full.txt: scripts/extract_candidates.awk  %.txt cards/mtg-data.txt
	awk -f $^ | sed 's/\t/\n/g'  > $@

cards/candidates_full.txt: scripts/extract_candidates.awk cards/scored_by_dec.tsv cards/unimplementable.tsv cards/mtg-data.txt
	awk -f $^ | sort -rg | sed 's/\t/\n/g' > $@

cards/unimplementable.tsv.update: cards/candidates_full.txt
	grep "|" $^ | sed 's/NAME://;s/|/\t/' >> $(basename $@)

%.out: $(MAG)
	SGE_TASK_ID=$* exp/eval_mcts.sh
	
M1.%: clean $(EXE) cubes release/Magarena/mods/felt_theme.zip
	grep "VERSION.*1.$*" -Ir src/
	grep "Release.*1.$*" release/README.txt
	grep 1.$* -Ir Magarena.app/
	-rm -rf Magarena-1.$*
	-rm -rf Magarena-1.$*.app
	-rm Magarena-1.$*.zip
	-rm Magarena-1.$*.app.zip
	mkdir -p Magarena-1.$*/Magarena/mods
	cp -r \
			release/gpl-3.0.html \
			release/Magarena.exe \
			release/Magarena.sh \
			release/Magarena.command \
			release/README.txt \
			release/lib \
			Magarena-1.$*
	cp -r \
			release/Magarena/avatars \
			release/Magarena/decks \
			release/Magarena/sounds \
			release/Magarena/scripts \
			Magarena-1.$*/Magarena
	cp \
			release/Magarena/mods/felt_theme.zip \
			release/Magarena/mods/*.txt \
			Magarena-1.$*/Magarena/mods
	-zip -r Magarena-1.$*.zip Magarena-1.$*
	cp -r Magarena.app Magarena-1.$*.app
	cd Magarena-1.$*.app/Contents/Resources; ln -s ../../../Magarena-1.$* Java
	chmod a+x Magarena-1.$*.app/Contents/MacOS/JavaApplicationStub
	-zip -r Magarena-1.$*.app.zip Magarena-1.$*.app

$(MAG): $(SRC)
	ant -f build.xml

class: $(BUILD)/javac.last

tags: $(SRC)
	ctags -R src

Test%.run: $(MAG)
	$(JAVAEA) -DtestGame=Test$* -Dmagarena.dir=`pwd`/release -jar $^ 2>&1 | tee Test$*.log

$(EXE): $(MAG)
	cd launch4j; ./launch4j ../release/magarena.xml

clean:
	-ant clean
	-rm -f $(BUILD)/javac.last
	-rm -f $(MAG)

clean/%: Magarena-%.zip Magarena-%.app.zip 
	-rm -rf Magarena-$*
	-rm -rf Magarena-$*.app
	-rm Magarena-$*.zip
	-rm Magarena-$*.app.zip

log.clean:
	-rm -f *.log

inf: $(MAG)
	-while true; do make `date +%s`.t; done

buildhive:
	make games=300 `date +%s`.t zips

games ?= 10000
str1 ?= 1
str2 ?= 1
life ?= 10
%.t: $(MAG)
	echo `hg id -n` > $*.log
	$(JAVA) -Dmagarena.dir=`pwd`/release magic.DeckStrCal \
	--seed $* \
	--ai1 MMAB --str1 ${str1} \
	--ai2 MMABC --str2 ${str2} \
	--life ${life} \
	--games 1 \
	--repeat ${games} >> $*.log 2>&1

test: $(MAG)
	-make `date +%s`.d

%.d: $(MAG)
	$(JAVAEA) -DrndSeed=$* -Dmagarena.dir=`pwd`/release -jar $^ |& tee $*.log

# Z = 4.4172 (99.999%)
# E = 0.01
# best estimator for r is p = h / (h + t)
# this estimator has a margin of error E, |p - r| < E at a particular Z, p - E < r < p + E
# n = Z^2 / 4E^2
#   = 48780
#   ~ 50000
%.str: $(MAG) release/Magarena/decks/JustRelentlessRats.dec release/Magarena/decks/LSK_G.dec
	$(JAVA) magic.DeckStrCal --deck1 $(word 2,$^) --deck2 $(word 3,$^) --ai1 $* --ai2 $* --games 50000 > $@

exp/%.log: $(MAG)
	scripts/evaluate_ai.sh $* > $@

decks/dl:
	for i in `curl http://www.wizards.com/magic/magazine/archive.aspx?tag=dailydeck | grep -o mtg/daily/deck/[0-9]* | cut -d'/' -f4`; do make decks/dd_$$i.dec; done
	for i in `curl http://www.wizards.com/magic/magazine/archive.aspx?tag=topdeck | grep -o mtg/daily/td/[0-9]* | cut -d'/' -f4`; do make decks/td_$$i.dec; done
	grep "name=" -r release/Magarena/incomplete | sed 's/.*name=/100 /' > decks/with_scripts.dec

decks/with_scripts.dec: $(wildcard release/Magarena/incomplete/*.txt)
	cat release/Magarena/incomplete/*.txt | grep "name=" | sed 's/.*name=//;s/^/100 /' | sort > $@

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

ref/rules.txt:
	curl `wget http://www.wizards.com/magic/rules -O - | grep txt | cut -d'"' -f4` | fmt -s > $@
	flip -u $@

resources/magic/data/icons/missing_card.png:
	convert -background gray -bordercolor black -border 5x5 -size 302x435 \
	-pointsize 30 label:'\nNo card image found\n\nSelect\n\"Download images\"\nfrom Arena menu\n\nOR\n\nSwitch to text mode\nusing the Enter key' $@

release/Magarena/mods/%_theme.zip: release/Magarena/mods/%_theme
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

cards/mtgo_cube.txt:
	wget -O - https://www.wizards.com/magic/magazine/article.aspx?x=mtg/daily/arcana/927 | grep autoCard | sed 's/<[^<]*>//g;s/^[ ]*//g' > $@

cards/mtgo_cube2.txt:
	wget -O - https://www.wizards.com/magic/magazine/article.aspx?x=mtg/daily/other/07032012d | grep autoCard | sed 's/<[^<]*>//g;s/^[ ]*//g' > $@

daily: $(EXE)
	mv $^ Magarena_`hg id -n`.exe
	scripts/googlecode_upload.py \
			-s "build `hg id -n`" \
			-p magarena \
			-u melvinzhang@gmail.com \
			-w `cat ~/Modules/notes/keys/googlecode_pw.txt` \
			-l Deprecated \
			Magarena_`hg id -n`.exe

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

cards/scriptable.txt: scripts/analyze_cards.scala scripts/effects.txt cards/cards.xml
	scala $^ > $@

grammar/rules.txt: scripts/normalize_rules.scala scripts/effects.txt cards/cards.xml
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

up:
	hg pull -u
	cd wiki; hg pull -u; cd ..

code_clones:
	~/App/pmd-bin-5.0-alpha/bin/run.sh cpd \
			--minimum-tokens 100 \
			--ignore-literals true \
			--ignore-identifiers true \
			--language java \
			--files src/magic/card > $@

cards/mtg-data:
	curl https://dl.dropbox.com/u/2771470/index.html | grep -o 'href="mtg.*.zip' | head -1 | sed 's/href="//' | xargs -I'{}' wget https://dl.dropbox.com/u/2771470/'{}'
	unzip -j mtg-data*.zip -d cards
	rm mtg-data*.zip

github/push:
	hg gexport
	git push origin master

unique_property:
	 grep "=" release/Magarena/scripts/*.txt| cut -d'=' -f1  | sort | uniq -c | sort -n

cards/scored_by_dec.tsv: cards/existing_tip.txt cards/unimplementable.tsv $(wildcard decks/*.dec)
	./scripts/score_card.awk decks/*.dec |\
	sort -rg |\
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
		name=$$(grep name= $^ | sed 's/name=//' | sed 's/ /%20/g');\
		value=$$(curl -sL http://gatherer.wizards.com/pages/card/details.aspx?name=$$name | grep "textRatingValue" | grep -o "[0-9]\.[^<]*");\
		sed -i "s/value=.*/value=$$value/" $^;\
	fi \

%.normalize: %
	flip -u $^
	make $^.update_value
	vim $^
	hg add $^

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

# meta check
checks: check_aura check_requires_card_code check_requires_groovy_code check_script_name check_unique_key

# every aura must have an enchant property
check_aura:
	diff \
	<(grep "subtype.*Aura" -lr release/Magarena/scripts | sort) \
	<(grep enchant= -lr release/Magarena/scripts | sort)

# every card that requires card code has a corresponding card class
# every card class has a corresponding card script that requires card code
check_requires_card_code:
	diff \
	<(ls -1 src/magic/card/*.java | cut -d'/' -f 4 | sed 's/.java//' | sort) \
	<(grep requires_card_code -r release/Magarena/scripts/ | sed 's/.*=//' | sed 's/,\([^ ]\)/\n\1/' | sed 's/.*scripts\///;s/.txt.*//' | sed 's/[^A-Za-z0-9]/_/g' | sort | uniq)

# every card that requires groovy code has a corresponding groovy script file
# every groovy script file has a corresponding card script that requires groovy code
check_requires_groovy_code:
	diff \
	<(ls -1 release/Magarena/scripts/*.groovy | cut -d'/' -f 4 | sed 's/.groovy//' | sort) \
	<(grep requires_groovy_code -r release/Magarena/scripts/ | sed 's/.*=//' | sed 's/,\([^ ]\)/\n\1/' | sed 's/.*scripts\///;s/.txt.*//' | sed 's/[^A-Za-z0-9]/_/g' | sort | uniq)

# script name is canonical card name
check_script_name:
	diff \
	<(ls -1 release/Magarena/scripts/ | grep txt | sort) \
	<(grep "name=" -r release/Magarena/scripts/ | sort | sed 's/.*name=//;s/[^A-Za-z0-9]/_/g;s/$$/.txt/')

check_unique_key:
	grep "^[^ ]*=" -r release/Magarena/scripts | sed 's/=.*//g' | sort | uniq -d

crash.txt: $(wildcard *.log)
	for i in `grep "^Excep" -l $^`; do \
		tail -n +`grep -n "random seed" $$i | tail -1 | cut -d':' -f1` $$i; \
	done >> $@
	rm $^

support/ui:
	for i in src/$@/*.java; do wget https://cakehat.googlecode.com/svn/trunk/$$i -O $$i; done

wiki/UpcomingCards.wiki: cards/new.txt
	echo "#summary New cards in the next release" > $@
	cat <(echo "{{{") $^ <(echo "}}}") >> $@

parser/test: $(MAG) 
	$(JAVA) magic.grammar.Check < grammar/parsable.txt

parser/test_all: $(MAG) grammar/rules.txt
	$(JAVA) magic.grammar.Check < $(word 2,$^)

parser/run: $(MAG)
	$(JAVA) magic.grammar.Check

grammar/parsable.txt: src/magic/grammar/MagicRuleParser.java
	make parser/test_all > grammar/test_all.out
	cat grammar/test_all.out | grep PARSED | sed 's/PARSED: //' | sort | uniq > $@
	cat grammar/test_all.out | grep FAILED | sort | uniq -c | sort -n > grammar/failed.txt

src/magic/grammar/MagicRuleParser.java: grammar/mtg.peg
	java -cp lib/Mouse-1.5.1.jar mouse.Generate -M -G $^ -P MagicRuleParser -S MagicSyntaxTree -p magic.grammar -r magic.grammar
	mv MagicRuleParser.java $@
	sed -i 's/accept()/sem.action() \&\& accept()/g' $@

grammar/CounterType: grammar/rules.txt
	grep -o "[^ ]* counter \(on\|from\)" $@ | cut -d' ' -f1 | sort | uniq > $@
	# remove a, each, that
	# add poison

fix_eol:
	sed -i -e '$$a\' release/Magarena/**/*.txt
	sed -i -e 's/\x0D$$//' release/Magarena/**/*.txt
	sed -i -e '$$a\' src/**/*.java

cards/cards_per_set.tsv: cards/existing_tip_full.txt
	cat <(grep -o ", [A-Z0-9]* [A-Z]" $^ | cut -d' ' -f2) \
	    <(grep -o "^[A-Z0-9]* [A-Z]"  $^ | cut -d' ' -f1) \
	| sort \
	| uniq -c \
	| sort -n \
	| sed 's/^ *//g;s/ /\t/' \
	> $@

%.convert:
	hg mv src/magic/card/$*.java release/Magarena/scripts/$*.groovy
	vim release/Magarena/scripts/$*.groovy
	sed -i 's/card_/groovy_/' release/Magarena/scripts/$*.txt
	hg commit -m "convert from java code to groovy code"
