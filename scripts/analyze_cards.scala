import scala.xml.pull._
import scala.xml._
import scala.io.Source

val abilities = List(
"@ attacks each turn if able.",
"@ can't block.",
"@ can't attack or block.",
"@ can't block creatures without flying.",
"@ can't be blocked by creatures with flying.",
"@ can't be blocked except by creatures with flying.",
"@ can't be blocked except by creatures with flying or reach.",
"@ can't be countered.",
"@ can't be the target of spells or abilities your opponents control.",
"hexproof",
"changeling",
"deathtouch",
"defender",
"double strike",
"exalted",
"fear",
"flash",
"flying",
"first strike",
"forestwalk",
"indestructible",
"@ is indestructible.",
"Islandwalk",
"haste",
"lifelink",
"mountainwalk",
"persist",
"plainswalk",
"protection from black",
"protection from blue",
"protection from green",
"protection from red",
"protection from white",
"protection from monocolored",
"protection from all colors",
"protection from creatures",
"protection from Demons",
"protection from Dragons",
"reach",
"shroud",
"swampwalk",
"trample",
"unblockable",
"@ is unblockable.",
"vigilance",
"wither",
"totem armor",
"intimidate",
"battle cry",
"infect",
"Living weapon",

"@'s power and toughness are each equal to the number of cards in your hand.",
"@'s power and toughness are each equal to the number of cards in all graveyards.",

"{T}: Add {G} to your mana pool.",
"{T}: Add {B} to your mana pool.",
"{T}: Add {R} to your mana pool.",
"{T}: Add {U} to your mana pool.",
"{T}: Add {W} to your mana pool.",
"{T}: Add {1} to your mana pool.",
"{T}: Add one mana of any color to your mana pool.",

"Deathtouch, lifelink",
"Defender, flying",
"Defender, flying, first strike",
"Defender, haste",
"Defender; reach",
"Defender, trample",
"Flying, deathtouch",
"Flying, first strike",
"Flying, first strike, haste",
"Flying, first strike, lifelink, protection from Demons and from Dragons",
"Flying, first strike, vigilance",
"Flying, first strike, vigilance, lifelink, protection from red and from green",
"Flying, first strike, vigilance, trample, haste, protection from black and from red",
"Flying, haste",
"Flying, double strike",
"Flying; fear",
"Flying, trample",
"Flying, vigilance",
"Flying, protection from red",
"Flying, protection from black",
"Flying, protection from green",
"Flying, protection from blue",
"Flying, protection from white",
"First strike, haste",
"First strike, protection from red",
"First strike, vigilance",

"Trample, haste",
"Trample, infect",
"Trample, protection from blue and from red",
"Trample; shroud",

"@ enters the battlefield tapped.",
"@ enters the battlefield with a +1/+1 counter on it.",
"@ enters the battlefield with two +1/+1 counters on it.",
"@ enters the battlefield with three +1/+1 counters on it.",
"@ enters the battlefield with four +1/+1 counters on it.",
"@ enters the battlefield with five +1/+1 counters on it.",
"@ enters the battlefield with six +1/+1 counters on it.",
"@ enters the battlefield with X +1/+1 counters on it.",
"@ enters the battlefield with two -1/-1 counters on it.",
"@ enters the battlefield with four -1/-1 counters on it.",
"@ enters the battlefield with three charge counters on it.",
"When @ enters the battlefield, draw a card.",
"When @ enters the battlefield, destroy target artifact.",
"Protection from artifacts",
"{W}, {T}: Tap target creature.",
"{T}: Prevent the next 1 damage that would be dealt to target creature or player this turn.",
"{T}: Prevent the next 2 damage that would be dealt to target creature or player this turn.",
"@ doesn't untap during your untap step.",
"@ can block only creatures with flying.",
"@ can't attack unless defending player controls an Island.",
"@ gets +1/+0 for each artifact you control.",
"@ gets +2/+2 for each Aura attached to it.",
"{R}: @ gets +1/+0 until end of turn.",
"{T}: @ deals 1 damage to target creature or player.",
"{B}: Regenerate @.",
"{B}: @ gets +1/+1 until end of turn.",
"{1}{B}: @ gets +1/+1 until end of turn.",
"{1}{R}: @ gets +1/+0 until end of turn.",
"{1}{B}: Regenerate @.",
"{G}: Regenerate @.",
"{1}{G}: Regenerate @.",
"{W}: @ gets +0/+1 until end of turn.",
"At the beginning of the end step, sacrifice @.",
"{2}, Remove a +1/+1 counter from @: Put a +1/+1 counter on target creature.",
"When @ enters the battlefield, you gain 4 life.",
"Wheneer @ attacks, it gets +2/+0 until end of turn.",
"Whenever @ deals damage, you gain that much life.",
"{R}: @ gets +1/-1 until end of turn.",
"When @ enters the battlefield, draw a card, then discard a card.",
"When @ enters the battlefield, put a 3/3 colorless Golem artifact creature token onto the battlefield.",
"When @ enters the battlefield, sacrifice a creature.",
"When @ enters the battlefield, you gain 3 life.",
"When @ enters the battlefield, tap enchanted creature.",
"When @ enters the battlefield, you gain 1 life.",
"Whenever @ attacks, it gets +2/+0 until end of turn.",
"Landfall — Whenever a land enters the battlefield under your control, @ gets +2/+2 until end of turn.",
"Whenever @ deals combat damage to a player, that player discards a card.",
"{U}: @ gains flying until end of turn.",
"When @ enters the battlefield, destroy target enchantment.",
"Metalcraft — @ gets +2/+2 as long as you control three or more artifacts.",
"As @ enters the battlefield, choose an opponent.",
"Whenever @ deals combat damage to a player, put a +1/+1 counter on it.",

"Equipped creature gets +2/+2.",
"Enchanted creature gets +2/+2.",
"Enchanted creature gets +3/+3.",
"Enchanted creature has flying.",
"Enchanted creature doesn't untap during its controller's untap step.",

"Enchant creature",
"Enchant creature you control",
"Enchant land",
"Enchant artifact",
"Enchant permanent",
"Enchanted creature can't attack.",
"Enchanted creature can't attack or block.",

"Draw a card.",
"Destroy target enchantment.",
"Destroy all enchantments.",
"Destroy target artifact.",
"Destroy target land.",
"Destroy target artifact or land.",
"Destroy target artifact or enchantment.",
"Enchanted creature gets +1/+1.",
"You control enchanted creature.",
"@ deals 1 damage to target creature or player.",
"@ deals 2 damage to target creature or player.",
"@ deals 3 damage to target creature or player.",
"When @ enters the battlefield, return a land you control to its owner's hand.",
"As @ enters the battlefield, you may pay 2 life. If you don't, @ enters the battlefield tapped.",
"Prevent all combat damage that would be dealt this turn.",
"Return target permanent to its owner's hand.",
"Return target creature card from your graveyard to your hand.",
"Target creature gets +2/+2 until end of turn.",
"{T}, Sacrifice @: Add one mana of any color to your mana pool.",
"{T}: Add {B} or {R} to your mana pool.",
"{T}: Add {G} or {W} to your mana pool.",
"{T}: Add {R} or {G} to your mana pool.",
"{T}: Add {U} or {B} to your mana pool.",
"{T}: Add {W} or {U} to your mana pool.",
"{T}: Put a charge counter on @.",
"{T}: Put a storage counter on @.",
"Target creature gets +4/+4 until end of turn.",

"Skip your draw step.",

"Counter target spell.",
"Counter target creature spell.",
"Counter target spell unless its controller pays {1}.",
"You have no maximum hand size.",
"Creatures you control get +1/+1 until end of turn.",
"Creatures you control have haste.",
"Target creature gains flying until end of turn.",
"Target creature gets +3/+3 until end of turn.",
"Target player discards a card.",

"Shadow",
""
)

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
    //Console.println(name.text)
    //Console.println(cost.text)
    if (loyalty.text != "") {
        //Console.println(loyalty.text)
    }
    if (pow.text != "") {
        //Console.println(pow.text + "/" + tgh.text)
    }
    var scriptable = (rules \ "rule").forall(x => isScriptable(name.text, x.text))
    if (scriptable) {
        Console.println(name.text);
        for (rule <- rules \ "rule") {
            Console.println(rule.text)
        }
        Console.println("-----")
    }
}

def isScriptable(name:String, rule:String):Boolean = {
    val norm = rule.trim.replace(name, "@")
    if (abilities.exists(x => x.equalsIgnoreCase(norm))) {
        return true
    }
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
