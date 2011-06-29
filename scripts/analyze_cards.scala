import scala.xml.pull._
import scala.xml._
import scala.io.Source

Console.println("loading " + args(0))
val src = XML.load(args(0))

for (card <- src \ "card") {
    val name = card \ "name"
    val types = card \ "typelist"
    val rules = card \ "rulelist"
    val cost = card \ "cost"
    val loyalty = card \ "loyalty"
    val pow = card \ "pow"
    val tgh = card \ "tgh"
    Console.println(name.text)
    Console.println(cost.text)
    if (loyalty.text != "") {
        Console.println(loyalty.text)
    }
    Console.println(pow.text + "/" + tgh.text)
    for (ctype <- types \ "type") {
        Console.println(ctype.text)
    }
    for (rule <- rules \ "rule") {
        Console.println(rule.text)
    }
    Console.println("-----")
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
