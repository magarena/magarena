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
    
    val types = card \ "typelist" \ "type"
    val rules = card \ "rulelist" \ "rule"
    
    rules.filter(_.text.trim() != "")
         .foreach(x => showNormalize(name, x.text))
}

//normalize the rule text
def showNormalize(name:String, rule:String):Unit = {
    //replace name with @
    val no_name = rule.trim.replace(name, "@")
    Console.println(no_name);
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
