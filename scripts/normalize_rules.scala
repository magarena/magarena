import scala.xml.pull._
import scala.xml._
import scala.io.Source

Console.err.println("loading cards from " + args(0))
val src = XML.load(args(0))

Console.err.println("begin analysis")
for (card <- src \ "card") {
    val name = (card \ "name").text
    val cost = (card \ "cost").text
    val loyalty = (card \ "loyalty").text
    val pow = (card \ "pow").text
    val tgh = (card \ "tgh").text
    
    val types = card \ "typelist" \ "type"
    val rules = card \ "rulelist" \ "rule" ++ card \ "multi" \ "rulelist" \ "rule"
    
    val normalized_rules = rules
         .filter(_.text.trim() != "")
         .map(x => normalize(name, x.text))
         .foreach(x => Console.println(x))
}
Console.err.println("complete analysis")

//normalize the rule text
def normalize(name:String, rule:String):String = {
    val nameBeforeComma = if (name.contains(',')) {
        name.substring(0, name.indexOf(','));
    } else {
        name
    }

    val abilityWord = if (!rule.startsWith("Choose") && rule.contains('—')) {
        rule.substring(0, rule.indexOf('—') + 1)
    } else {
        name
    }

    rule
        .trim
        .replace(name, "@")
        .replace(nameBeforeComma, "@")
        .trim
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
