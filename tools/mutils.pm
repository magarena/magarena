use strict;

#This module contains common function

# List of allowed editions
my %eds=();

# Load list of allowed editions
# Parameter 1: filename - list of allowed editions
sub load_editions {
 my $filename=shift;
 open P,"<$filename";
 while (<P>) {
  s/[\r\n]+//gm;
  next if (/^#/);    # Comment
  next if (/^\s+$/); # Empty line
  $eds{$_}=1;
 }
 close P;
}

# Check if the set/rarity of a card contains at least one allowed edition
# Parameter 1: set/rarity of a card (for example "Mirrodin common, Ninth Rare")
# Returns: True if at least one of the editions is allowed
sub allowed_edition {
 my $ed=shift;
 my @edl=split(/,\s+/,$ed);
 foreach my $e (@edl) {
  $e=~s/ (Mythic Rare|Rare|Uncommon|Common)$//;
  if ($eds{$e}) {return 1;}
 }
 return 0;
}

# Return list of editions having this card
# Parameter 1: Name of card
# Returns: Array with all editions containing the card
sub edition_list {
 my $ed=shift;
 my @edl=split(/,\s+/,$ed);
 my @out=();
 foreach my $e (@edl) {
  $e=~s/ (Mythic Rare|Rare|Uncommon|Common)$//;
  push @out,$e;
 }
 return @out;
}

# Load list of cards and return them in an associative array
# Parameter 1: Name of file with card list
# Returns: List as associative array
sub load_list {
 my $filename=shift;
 my %lst=();
 open P,"<$filename";
 while (<P>) {
  s/[\r\n]+//gm;
  $lst{$_}=1;
 }
 close P;
 return %lst;
}

# Return default configuration
sub default_config {
 my %config=();
 # Root of repository relative to directory with tools
 $config{'root'}='../';

 # File with card data
 $config{'cardfile'}=$config{'root'}.'resources/magic/data/cards.txt';
 # File with images (cards, HQ cards and others)
 $config{'imagefile'}=$config{'root'}.'resources/magic/data/images.txt';

 # List of all cards from gatherer, in text spoiler format. Available from url:
 # http://gatherer.wizards.com/Pages/Search/Default.aspx?output=spoiler&method=text&action=advanced&type=%7c%5b%22Creature%22%5d%7c%5b%22Artifact%22%5d%7c%5b%22Land%22%5d%7c%5b%22Instant%22%5d%7c%5b%22Legendary%22%5d%7c%5b%22Basic%22%5d%7c%5b%22Enchantment%22%5d%7c%5b%22Ongoing%22%5d%7c%5b%22Plane%22%5d%7c%5b%22Planeswalker%22%5d%7c%5b%22Scheme%22%5d%7c%5b%22Snow%22%5d%7c%5b%22Sorcery%22%5d%7c%5b%22Tribal%22%5d%7c%5b%22Vanguard%22%5d%7c%5b%22World%22%5d
 $config{'cardhtml'}='lists/mtg_all_list.html';

 # List of cards from magiccards.info. There is no way to get all cards as single list,
 # so this list have to be created by several requests (all common creatures, all uncommon creatures, etc ...) and then concatenating results together
 # http://magiccards.info
 $config{'hqhtml'}='lists/hq_creatures.html';

 # Data for automatic cube generation
 $config{'cubegen_data'}='cube_gen.txt';

 # Card edition contents
 $config{'edition_contents'}='_editions_contents.txt';

 # File with list of names of implemented cards
 $config{'cardlist'}='_cardlist.txt';

 return %config;
}

# Normalize card type and possibly fills up subtype
# Parameter 1: Reference to hash wuith card data to modify
sub normalize_type {
 my ($k)=@_;
 my $type=$k->{'Type'};
 $type=~s/Legendary Creature/Legendary,Creature/;
 $type=~s/Artifact Creature/Artifact,Creature/;
 my $subtype='';
 if ($type=~/^(.+) — (.+)$/) {
  $type=$1;
  $subtype=$2;
  $k->{'Subtype'}=$subtype;
 }
 $k->{'Type'}=$type;
}

# Return spell color based on its mana cost
# Parameter 1: Spell mana cost
# Returns: spell color
sub spell_color {
 my $cost=shift;
 my $c_color='';
 if ($cost=~/R/) {$c_color.='r';}
 if ($cost=~/G/) {$c_color.='g';}
 if ($cost=~/B/) {$c_color.='b';}
 if ($cost=~/U/) {$c_color.='u';}
 if ($cost=~/W/) {$c_color.='w';}
 return $c_color;
}

# Normalize rule text. Mostly remove explanatory comments from it.
# Parameter 1: rule text
# Parameter 2: card name
# Returns: normalized text
sub normalize_ruletext {
 my $txt=shift;
 my $name=shift;
 $txt=~s/Protection from ([a-z]+) and from ([a-z]+)/Protection from $1, Protection from $2/i;
 $txt=~s/\(\{\(.\/.\)\} can be paid with either \{.\} or \{.\}\.\)//i;
 $txt=~s/Defender, flying \(This creature can't attack, and it can block creatures with flying.\)/Defender, flying/i;
 $txt=~s/Intimidate \(This creature can't be blocked except by artifact creatures and\/or creatures that share a color with it.\)/Intimidate/i;
 $txt=~s/$name attacks each turn if able\./attacks each turn if able/i;
 $txt=~s/$name can't be countered\./can't be countered/i;
 $txt=~s/$name can't be blocked except by creatures with flying\./can't be blocked except by creatures with flying/i;
 $txt=~s/$name is indestructible\.( \((Lethal damage and effects that say "destroy" don't destroy it. If its toughness is 0 or less, it's still put into its owner's graveyard|"Destroy" effects and lethal damage don't destroy it|Effects that say "destroy" don't destroy it).\))?/indestructible/i;
 $txt=~s/$name can't block\./can't block/i;
 $txt=~s/$name can't attack or block\./can't attack or block/i;
 $txt=~s/$name can block only creatures with flying\./can't block creatures without flying/i;
 $txt=~s/$name is unblockable\./unblockable/i;
 $txt=~s/Fear \(This creature can't be blocked except by artifact creatures and\/or black creatures.\)/Fear/i;
 $txt=~s/First strike \(This creature deals combat damage before creatures without first strike.\)/First strike/i;
 $txt=~s/Defender \(This creature can't attack.\)/Defender/;
 $txt=~s/Banding \(Any creatures with banding, and up to one without, can attack in a band. Bands are blocked as a group. If any creatures with banding you control are blocking or being blocked by a creature, you divide that creature's combat damage, not its controller, among any of the creatures it's being blocked by or is blocking.\)/Banding/i;
 $txt=~s/Shadow \(This creature can block or be blocked by only creatures with shadow.\)/Shadow/i;
 $txt=~s/Double strike \(This creature deals both first-strike and regular combat damage.\)/Double strike/i;
 $txt=~s/Vigilance \(Attacking doesn't cause this creature to tap.\)/Vigilance/i;
 $txt=~s/Whenever $name deals damage, you gain that much life./Lifelink/i;
 $txt=~s/Deathtouch \(Creatures dealt damage by this creature are destroyed. You can divide this creature's combat damage among any of the creatures blocking or blocked by it\.\)/Deathtouch/i;
 $txt=~s/Flash \(You may cast this spell any time you could cast an instant.\)/Flash/i;
 $txt=~s/Flying \(This creature can't be blocked except by creatures with flying or reach.\)/Flying/i;
 $txt=~s/Persist \(When this creature is put into a graveyard from the battlefield, if it had no -1\/-1 counters on it, return it to the battlefield under its owner's control with a -1\/-1 counter on it.\)/Persist/i;
 $txt=~s/Shroud \(This (creature|permanent) can't be the target of spells or abilities.\)/Shroud/i;
 $txt=~s/Lifelink \(Damage dealt by this creature also causes you to gain that much life.\)/Lifelink/i;
 $txt=~s/Changeling \(This card is every creature type at all times.\)/Changeling/i;
 $txt=~s/Exalted \(Whenever a creature you control attacks alone, that creature gets \+1\/\+1 until end of turn.\)/Exalted/i;
 $txt=~s/Trample \(If this creature would (deal|assign) enough damage to its blockers to destroy them, you may have it (deal|assign) the rest of its damage to defending player or planeswalker.\)/Trample/i;
 $txt=~s/Haste \(This creature can attack and \{T\} as soon as it comes under your control.\)/Haste/i;
 $txt=~s/Haste \(This creature can attack the turn it comes under your control.\)/Haste/i;
 $txt=~s/reach \(This (creature )?can block creatures with flying.\)/Reach/i;
 $txt=~s/Flanking \(Whenever a creature without flanking blocks this creature, the blocking creature gets -1\/-1 until end of turn.\)/Flanking/i;
 $txt=~s/Wither \(This deals damage to creatures in the form of -1\/-1 counters.\)/Wither/i;
 $txt=~s/Deathtouch \(Any amount of damage this deals to a creature is enough to destroy it\.\)/Deathtouch/i;
 $txt=~s/(Swamp|Forest|Island|Plains|Mountain)walk \(This creature is unblockable as long as defending player controls an? \1.\)/${1}walk/i;
 $txt=~s/^<br \/>//i;
 return $txt;
}

1
