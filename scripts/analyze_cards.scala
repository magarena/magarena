import scala.xml.pull._
import scala.xml._
import scala.io.Source

Console.err.println("loading effects from " + args(0))
val effects = Source.fromFile(args(0)).getLines.toList

Console.err.println("loading cards from " + args(1))
val src = XML.load(args(1))

Console.err.println("loading existing from " + args(2))
val existing = Source.fromFile(args(2)).getLines.toSet

Console.err.println("begin analysis")
for (card <- src \ "card") {
    val name = (card \ "name").text.replace("Æ", "AE").replace("û","u").replace("ö","o");
    val cost = (card \ "cost").text
    val loyalty = (card \ "loyalty").text
    val pow = (card \ "pow").text
    val tgh = (card \ "tgh").text
    
    val types = card \ "typelist" \ "type"
    val rules = card \ "rulelist" \ "rule"
    
    var scriptable = rules
        .filter(_.text.trim() != "")
        .forall(x => isScriptable(name, x.text))
    
    if (existing contains name) {
        Console.println("name=" + name);
        //Console.println("value=3")
        //Console.println("rarity=C")

        val typeStr = types
        .filter(x => (x \ "@type").text == "card" || (x \ "@type").text == "super")
        .map(_.text)
        .mkString(",")
        
        Console.println("type=" + typeStr);

        val subtypeStr = types
        .filter(x => (x \ "@type").text == "sub")
        .map(_.text)
        .mkString(",")

        if (subtypeStr.length > 0) {
            Console.println("subtype=" + subtypeStr)
        }
   
        if (cost.length > 0) {
            Console.println("cost=" + cost)
        }

        if (pow.length > 0 && tgh.length > 0) {
            Console.println("pt=" + pow + "/" + tgh)
        }
        //Console.println("timing=main")
        for (rule <- rules if rule.text.trim != "") {
            //Console.println("ability=" + rule.text)
        }
        Console.println()
    }
}
Console.err.println("end analysis")

def isScriptable(name:String, rule:String):Boolean = {
    //normalize the rule text

    //replace name with @
    //remove cost
    val norm = rule.trim.replace(name, "@").replaceAll("^[^\\\"]*: ", "")

    //check if this is in the engine
    if (effects.exists(x => x.equalsIgnoreCase(norm))) {
        return true
    }

    //if not, print it out
    //Console.err.println(norm + "\t" + name);
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
