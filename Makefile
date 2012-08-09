JAVAEA=java -ea -Xms256M -Xmx256M -Ddebug=true
LIBS=.:lib/annotations.jar:lib/jsr305.jar
JAVA=${JAVAEA} -Dcom.sun.management.jmxremote -cp $(LIBS):release/Magarena.jar
SHELL=/bin/bash
BUILD=build
JOPTS=-Xlint:all -d $(BUILD) -cp $(LIBS):$(BUILD):.
SRC=$(shell find src -iname *.java)
MAG:=release/Magarena.jar
EXE:=release/Magarena.exe

all: $(MAG) $(EXE) tags

zips:
	make M`grep Release release/README.txt | head -1 | cut -d' ' -f2`

check_literals:
	grep "\"" src/magic/card/* | awk -f scripts/check_literals.awk

cubes: \
	release/Magarena/mods/legacy_cube.txt \
	release/Magarena/mods/extended_cube.txt \
	release/Magarena/mods/standard_cube.txt \
	release/Magarena/mods/modern_cube.txt

themes: \
	release/Magarena/mods/felt_theme.zip \
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

code_to_remove: $(MAG)
	cat src/magic/card/*.java | sed 's/\s\+//g' | sed 's/(.*)/(...)/g' | sort | uniq -c | sort -n | grep publicstaticfinal | grep ");" > $@

casts: $(MAG)
	grep -n "([A-Z]\+[a-z]\+[A-Za-z]*)" -r src/ | flip -u > $@

warnings_H.txt: warnings.txt
	grep "(H)" $^ | grep -v System.out | grep -v System.err | grep -v EXS > $@

warnings.txt: $(MAG)
	~/App/findbugs/bin/findbugs \
			-textui \
			-progress \
			-sortByClass \
			-emacs \
			-effort:max \
			-output $@ \
			-sourcepath src \
			build

cards/legacy_banned.txt:
	curl https://www.wizards.com/Magic/TCG/Resources.aspx?x=judge/resources/sfrlegacy | grep nodec | grep -o ">[^<]*</a" | sed 's/>//g;s/<\/a//;' > $@

release/Magarena/mods/legacy_cube.txt: cards/existing_tip.txt cards/legacy_banned.txt
	join -v1 -t"|" <(sort $(word 1,$^)) <(sort $(word 2,$^)) > $@

release/Magarena/mods/%_cube.txt: cards/existing_tip.txt cards/%_all.txt
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
	sed -i 's/Æ/AE/' $@

cards/extended_all.txt:
	curl "http://magiccards.info/query?q=cmc%3D0+f%3Aextended&s=cname&v=olist" | grep "en/" | sed 's/<[^>]*>//g' > $@
	curl "http://magiccards.info/query?q=cmc%3D1+f%3Aextended&s=cname&v=olist" | grep "en/" | sed 's/<[^>]*>//g' >> $@
	curl "http://magiccards.info/query?q=cmc%3D2+f%3Aextended&s=cname&v=olist" | grep "en/" | sed 's/<[^>]*>//g' >> $@
	curl "http://magiccards.info/query?q=cmc%3D3+f%3Aextended&s=cname&v=olist" | grep "en/" | sed 's/<[^>]*>//g' >> $@
	curl "http://magiccards.info/query?q=cmc%3D4+f%3Aextended&s=cname&v=olist" | grep "en/" | sed 's/<[^>]*>//g' >> $@
	curl "http://magiccards.info/query?q=cmc%3D5+f%3Aextended&s=cname&v=olist" | grep "en/" | sed 's/<[^>]*>//g' >> $@
	curl "http://magiccards.info/query?q=cmc>%3D6+f%3Aextended&s=cname&v=olist" | grep "en/" | sed 's/<[^>]*>//g' >> $@
	sed -i 's/Æ/AE/' $@

cards/standard_all.txt:
	curl "http://magiccards.info/query?q=f%3Astandard&s=cname&v=olist&p=1" | grep "en/" | sed 's/<[^>]*>//g' > $@
	curl "http://magiccards.info/query?q=f%3Astandard&s=cname&v=olist&p=2" | grep "en/" | sed 's/<[^>]*>//g' >> $@
	sed -i 's/Æ/AE/' $@

cards/new_%.txt: cards/existing_tip.txt cards/existing_%.txt
	join -v1 -t"|" <(sort $(word 1,$^)) <(sort $(word 2,$^)) > $@

cards/new_scripts_%.txt: release/Magarena/scripts
	grep "name=" -h $$(hg diff -r $* | grep -B 1 "^--- /dev/null" | grep $^ | cut -d' ' -f4) | sed 's/name=//' > $@
	flip -u $@

cards/existing_scripts_%.txt: $(wildcard release/Magarena/scripts/*.txt)
	hg cat -r $* release/Magarena/scripts | grep "^name=" | sed 's/name=//' | sort > $@
	sed -i 's/\r//' $@

cards/existing_tokens_%.txt: $(wildcard release/Magarena/scripts/*.txt)
	hg cat -r $* release/Magarena/scripts | grep -C 1 "^token=" | grep "^name=" | sed 's/name=//' | sort > $@

cards/existing_%.txt: cards/existing_scripts_%.txt cards/existing_tokens_%.txt
	join -v1 -t"|" <(sort $(word 1,$^)) <(sort $(word 2,$^)) > $@

%_full.txt: %.txt cards/mtg-data.txt
	awk -f scripts/extract_existing.awk $^ > $@

cards/candidates_full.txt: scripts/extract_candidates.awk cards/scored_by_dd.tsv cards/mtg-data.txt
	awk -f $^ | sort -rg | sed 's/\t/\n/g' > $@

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
	cp \
			release/gpl-3.0.html \
			release/Magarena.exe \
			release/Magarena.sh \
			release/Magarena.command \
			release/README.txt \
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

$(BUILD)/javac.last: $(SRC)
	-mkdir $(BUILD)
	javac $(JOPTS) $?
	cp -r resources/* $(BUILD)
	touch $@

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

inf: $(MAG)
	-while true; do make `date +%s`.t; done

%.t: $(MAG)
	echo `hg id -n` > $*.log
	$(JAVAEA) -DrndSeed=$* -DselfMode -jar $^ >> $*.log 2>&1

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

decks/dd:
	for i in `curl http://www.wizards.com/magic/magazine/archive.aspx?tag=dailydeck | grep -o mtg/daily/deck/[0-9]* | cut -d'/' -f4`; do make decks/dd_$$i.dec; done

decks/td:
	for i in `curl http://www.wizards.com/magic/magazine/archive.aspx?tag=topdeck | grep -o mtg/daily/td/[0-9]* | cut -d'/' -f4`; do make decks/td_$$i.dec; done

# Daily Deck
decks/dd_%.dec: scripts/dailyhtml2dec.awk
	curl "http://www.wizards.com/Magic/Magazine/Article.aspx?x=mtg/daily/deck/$*" | awk -f $^ > $@

# Mike Flores
decks/mf_%.dec: scripts/dailyhtml2dec.awk
	curl http://www.wizards.com/Magic/Magazine/Article.aspx?x=mtgcom/daily/mf$* | awk -f $^ > $@

# Top Decks
decks/td_%.dec: scripts/dailyhtml2dec.awk
	curl http://www.wizards.com/Magic/Magazine/Article.aspx?x=mtg/daily/td/$* | awk -f $^ > $@

decks/ml_%.dec: scripts/apprentice2dec.awk
	wget "http://www.magic-league.com/decks/download.php?deck=$*&index=1" -O - | flip -u - | awk -f $^ > $@

ref/rules.txt:
	curl http://www.wizards.com`wget http://www.wizards.com/magic/rules -O - | grep txt | cut -d'"' -f4` | fmt -s > $@
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

upload/%: 
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

cards/with_card_code:
	ls -1 src/magic/card/*.java | cut -d'/' -f 4 | sed 's/.java//' | sort > $@

cards/require_card_code:
	 grep requires_card_code release/Magarena/scripts/* | cut -d'/' -f4 | sed 's/.txt:.*//' | sort > $@

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

cards/scored_by_dd.tsv: cards/existing_tip.txt $(wildcard decks/dd*)
	./scripts/score_card.awk `ls -1 decks/dd* | sort -n -t_ -k2` |\
	sort -rg |\
	./scripts/keep_unimplemented.awk $(word 1,$^) /dev/stdin  > $@

cards/scored_by_td.tsv: cards/existing_tip.txt $(wildcard decks/td*)
	./scripts/score_card.awk `ls -1 decks/td* | sort -n -t_ -k2` |\
	sort -rg |\
	./scripts/keep_unimplemented.awk $(word 1,$^) /dev/stdin  > $@
	
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

gatherer_rankings: cards/existing_tip.txt
	IFS=$$'\n'; for i in `cat $^`; do\
		echo -n -e "$$i\t";\
		echo $$(curl -s http://gatherer.wizards.com/pages/card/details.aspx?name=`echo $$i | sed 's/ /%20/g'` | grep "textRatingValue" | grep -o "[0-9]\.[^<]*");\
	done > $@

update_value_from_rankings:
	for i in release/Magarena/scripts/*; do \
		sed -i "s/value=.*/value=$$(join -t'	' <(head -1 $$i | sed 's/name=//') gatherer_rankings | cut -f2)/" $$i;\
	done
		#sed -i "s/value=$$/value=1/" $$i;\

