import scala.xml.pull._
import scala.xml._
import scala.io.Source

/*
reads the metadata for cards

<metalist>
 <card>
  <name>A Display of My Dark Power</name>
  <instance>
   <set>ARC</set>
   <rarity>C</rarity>
   <number>8</number>
   <artist>Jim Nelson</artist>
   <flavor-text>&quot;What would you say is my greatest attribute? Is it my gluttonous lust for power?&quot;</flavor-text>
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

var arg_idx = 0
Console.err.println("loading meta.xml from " + args(arg_idx))
val meta = XML.load(args(arg_idx))

arg_idx += 1

Console.err.println("loading cards.txt from " + args(arg_idx))
val cards = Source.fromFile(args(arg_idx)).getLines.toList

var name2rarity = new scala.collection.mutable.HashMap[String, String]()

Console.err.println("reading rarity from meta.xml")
for (card <- meta \ "card") {
    val name_node = card \ "name"
    val name = name_node.text.replace("Æ", "AE").replace("û","u").replace("ö","o");
    val instance = card \ "instance"
    val rarity_node = instance \ "rarity"
    name2rarity += (name -> rarity_node.text)
}

Console.err.println("reading cards.txt")
var curr_name = ""
for (line <- cards) {
    if (line startsWith ">") {               //card name
        curr_name = line.substring(1);
    } 
    
    if (line startsWith "rarity=") {  //rarity char
        val curr_rarity = line charAt 7

        if (!name2rarity.contains(curr_name)) {
            Console.err.println(curr_name + " NOT FOUND in meta.xml");
        } else {
            val rarity_node = name2rarity(curr_name)
            val real_rarity = rarity_node.filter(x => "LCMRU".indexOf(x) >= 0) charAt 0
            if (real_rarity != curr_rarity) {
                Console.err.println(curr_name + 
                    " Curr: " + curr_rarity + 
                    " Real: " + real_rarity)
                Console.println("rarity=" + real_rarity)
            } else {
                Console.println(line) 
            }
        }
    } else {
        Console.println(line)
    }
}
