import scala.xml.pull._
import scala.xml._
import scala.io.Source

/*
reads the metadata for cards

<metalist>
 <card name="A Display of My Dark Power">
  <instance>
   <set>Archenemy</set>
   <rarity>C</rarity>
   <number>8</number>
   <artist>Jim Nelson</artist>
  </instance>
 </card>
...
</metalist>
*/

/*
Reads cards.txt and fix rarity

>Scion of the Wild
url=http://magiccards.info/10e/en/295.html
image=http://magiccards.info/scans/en/10e/295.jpg
value=3
rarity=R
type=Creature
subtype=Avatar
color=g
converted=3
cost={1}{G}{G}
timing=smain
*/

Console.err.println("loading meta.xml from " + args(0))
val meta = XML.load(args(0))

var name2card = new scala.collection.mutable.HashMap[String, Node]()

Console.err.println("reading rarity from meta.xml")
for (card <- meta \ "card") {
    val name_node = card \ "@name"
    val name = name_node.text
    name2card += (name -> card)
}

var curr_name = ""
for (line <- Source.stdin.getLines) {
    //Console.println(line)

    //card name
    if (line startsWith "name=") {    
        curr_name = line.substring(5);
    //rarity char
    } else if (line startsWith "rarity=") {  
        val curr_rarity = line charAt 7

        if (!name2card.contains(curr_name)) {
            Console.err.println(curr_name + " NOT FOUND in meta.xml");
        } else {
            if ((name2card(curr_name) \\ "rarity").exists(x => x.text.charAt(0) == curr_rarity)) {
                //Console.err.println("found")
            } else {
                Console.out.println(curr_name + "'s rarity should not be " + curr_rarity) 
            }
        }
    } 
}
