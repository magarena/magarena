import scala.xml.pull._
import scala.xml._
import scala.io.Source

Console.err.println("loading effects from " + args(0))
val effects = Source.fromFile(args(0)).getLines.toList

Console.err.println("loading cards from " + args(1))
val src = XML.load(args(1))

Console.err.println("begin analysis")
for (card <- src \ "card") {
    val name = (card \ "name").text
    val cost = (card \ "cost").text
    val loyalty = (card \ "loyalty").text
    val pow = (card \ "pow").text
    val tgh = (card \ "tgh").text
    
    val types = card \ "typelist"
    val rules = card \ "rulelist"
    
    var scriptable = (rules \ "rule").filter(_.text.trim() != "").forall(x => isScriptable(name, x.text))
    if (scriptable) {
        Console.println(">" + name);
        /*
        Console.println("value=?")
        Console.println("rarity=?")
        Console.println("type=Creature")
        Console.println("subtype=Beast,Human")
        Console.println("color=g")
        Console.println("converted=6")
        Console.println("cost={4}{G}{G}")
        Console.println("power=6")
        Console.println("toughness=5")
        Console.println("timing=main")
        */
        for (rule <- rules \ "rule" if rule.text.trim != "") {
            Console.println(rule.text)
        }
    }
}

def isScriptable(name:String, rule:String):Boolean = {
    //normalize the rule text
    val norm = rule.trim.replace(name, "@")

    //check if this is in the engine
    if (effects.exists(x => x.equalsIgnoreCase(norm))) {
        return true
    }

    //if not, print it out
    Console.err.println(norm + "\t" + name);
    return false
}

/*
Contains the information about the cards that is needed to play the
game.

<cardlist>
  <card>
    <name></name>
    <cost></cost>
    <loyalty></loyalty>
    <typelist>
      <type></type>
      ...
    </typelist>
    <pow></pow>
    <tgh></tgh>
    <hand></hand>
    <life></life>
    <rulelist>
      <rule reminder=""></rule>
      ...
    </rulelist>
    <multi type="">
      ...
    </multi>
  </card>
  ...
</cardlist>
*/
