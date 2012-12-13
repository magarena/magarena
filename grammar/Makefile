test: Check.class
	java -cp Mouse-1.5.1/build/Mouse-1.5.1.jar:. Check < parsable.txt

test_all: Check.class
	java -cp Mouse-1.5.1/build/Mouse-1.5.1.jar:. Check < rules.txt

run: Check.class
	java -cp Mouse-1.5.1/build/Mouse-1.5.1.jar:. Check

parsable.txt: mtg.peg
	make test_all > test_all.out
	cat test_all.out | grep PARSED | sed 's/PARSED: //' | sort | uniq > $@
	cat test_all.out | grep FAILED | sort | uniq -c | sort -n > failed.txt

MagicRuleParser.java: mtg.peg
	-rm *.class $@
	java -cp Mouse-1.5.1/build/Mouse-1.5.1.jar mouse.Generate -M -G $^ -P MagicRuleParser -S MagicSyntaxTree
	sed -i 's/accept()/sem.action() \&\& accept()/g' $@

Check.class: MagicRuleParser.java MagicSyntaxTree.java Check.java
	javac -cp Mouse-1.5.1/build/Mouse-1.5.1.jar *.java

CounterType:
	grep -o "[^ ]* counter \(on\|from\)" rules.txt | cut -d' ' -f1 | sort | uniq > $@
	# remove a, each, that
	# add poison
