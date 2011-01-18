#!/usr/bin/perl
# Parse cards from a list and output those cards, that can be automatically imported without changing the Java code

use strict;
use mutils;

my %config=default_config();

# Blacklist - list of cards not to be implemented - for example ante cards
my $blacklist='blacklist.txt';

# List of allowed editions
my $editions='editions.txt';

# Output - list of cards that were not fully understood by this parser
my $cardlist='_unparsed_cards.txt';

# Output - parts of rule text not understood by this parser
my $errorlog='_unparsed_code.txt';

# Output - definitions of newly imported cards 
my $cardout='_cards.txt';
# Output - images of newly imported cards 
my $imageout='_images.txt';
# Output - high quality images of newly imported cards 
my $hqimageout='_hqimages.txt';

# List of cards to skip
my %skip=();

# Skip all cards listed in given file when importing.
sub skip_cards {
 my $filename=shift;
 open P,"<$filename";
 while (<P>) {
  s/[\r\n]+//gm;
  s/#.*//;
  s/^\s+//gm;
  s/\s+$//gm;
  next if (/^$/);
  $skip{$_}=1;
 }
 close P;
}

skip_cards($config{'cardlist'});
skip_cards($blacklist);
load_editions($editions);

# Links to high quality images
my %hqim=();

# Load file with information about high-quality images
open H,"<".$config{'hqhtml'};
binmode H;
while (<H>) {
 if (m#<td><a href="/([a-z0-9]+)/([a-z0-9]+)/(\d+).html">([Æûáâàíäöú\-\._'a-zA-Z, \(\)"\?]+)</a></td>#) {
  my $url="http://magiccards.info/scans/$2/$1/$3.jpg";
  $hqim{$4}=$url;
  #http://magiccards.info/5e/en/277.html -> http://magiccards.info/scans/en/5e/277.jpg
 } else {
  if (m#<td><a href="[^"]+">[^<>]+</a></td>#) {
   print STDERR "Unrecognized HQ card: $_";
  }

 }
}
close H;

#Number of cards imported
my $numc=0;
#Number of cards skipped because not in allowed edition
my $nume=0;

#Card data for current card
my %k=();

open EDI,">".$config{'edition_contents'};
binmode EDI;
open F,"<".$config{'cardhtml'};
binmode F;
open O,">$cardlist";
binmode O;
open OU,">$cardout";
binmode OU;
open ER,">$errorlog";
binmode ER;
open IO,">$imageout";
binmode IO;
open IOH,">$hqimageout";
binmode IOH;
sub emit {
 my $name=$k{'Name'};
 if (!$name=~/./) {return;}
 my $rarity=$k{'Set/Rarity'};
 my @ed=edition_list($rarity);
 foreach my $e (@ed) {
  print EDI "$name|$e\n";
 }
 if ($skip{$name}) {
  %k=();
  return;
 }
 if (!allowed_edition($rarity)) {
  $nume++;
  return;
 }
 normalize_type(\%k);
 my $txt=normalize_ruletext($k{'Rules Text'},$name);
 $k{'Rules Text'}=$txt;
 my $cost=$k{'Cost'};
 my $complex=1;
 my $type=$k{'Type'};
 my $subtype=$k{'Subtype'};
 if ($type=~/^(Legendary,|Artifact,|)Creature$/) { 
  my $complex=0;
  my @abil=();
  my %haveabil=();
  my $pt=$k{'Pow/Tgh'};
  my $pow='';
  my $tou='';
  if ($pt=~/^\((.*)\/(.*)\)$/) {
   $pow=$1;
   $tou=$2;
   $k{'Pow/Tgh'}="$pow/$tou";
  }
  if ($rarity=~/rare/i) {$rarity=3;}
  if ($rarity=~/uncommon/i) {$rarity=2;}
  if ($rarity=~/common/i) {$rarity=1;}
  if (!($pow=~/^[0-9]+$/)) {$complex=1;}
  if (!($tou=~/^[0-9]+$/)) {$complex=1;}
  while ($txt=~s/^(can't block creatures without flying|can't be blocked except by creatures with flying|can't attack or block|attacks each turn if able|can't be countered|can't block|unblockable|changeling|deathtouch|defender|double strike|exalted|fear|first strike|flash|flying|forestwalk|haste|indestructible|intimidate|islandwalk|lifelink|mountainwalk|plainswalk|persist|protection from (Demons|Dragons|all colors|black|blue|creatures|green|monocolored|red|white)|reach|shroud|swampwalk|trample|vigilance|wither)\s*(,|;|<br\s*\/?>|)\s*//i) {
#battle cry
#can't be blocked by creatures with flying
#can't be the target of spells or abilities your opponent controls
#totem armor
   push @abil,lc $1;
   $haveabil{lc $1}=1;
  }
  $txt=~s/^<br \/>//i;
  my $abils=join(',',@abil);
  if ($txt=~/./) {
   $complex=1;
   $k{'Complex rules'}=$txt;
   $txt=~s/<br \/>/\n/gm;
   $txt=~s/$name/(THIS)/gm;
   $txt=~s/(\{\([rgbuw]\/[rgbuw]\))*(\{([0-9]+|[RGBUWX]|\([rgbuw]\/[rgbuw]\))\})+/(MANACOST)/gm;
   print ER $txt."\n";
  }
  if ($cost eq '') {$complex=1;}
  if (!$complex) {
   $subtype=~s/ /,/g;
   $numc++;
   if ($abils=~/./) {$abils='ability='.$abils."\n";}
   my $timing='main';
   if ($abils=~/haste|can.t block|shroud/) {$timing="fmain";}
   if ($abils=~/defender/) {$timing="smain";}
   my $c_conv=0;
   my $c_cost='';
   if ($cost=~/^(\d+)?((\([RGBUW]\/[RGBUW]\))*)?([RGBUW]+)?$/) {
    my $num=$1;
    my $xcol=$2;
    $xcol=~tr/()/{}/;
    my $col=$4;
    if (length($num)) {
     $c_conv+=$num;
     $c_cost='{'.$num.'}';
    }
    if ($col) {
     $c_conv+=length $col;
     $c_cost.='{'.join('}{',split(//,$col)).'}';
    }
    if ($xcol) {
     $c_conv+=(length $xcol)/5;
     $c_cost.=$xcol;
    }
   } else {
    print STDERR "Unknown casting cost: $cost\n";
   }
   my $c_color=spell_color($cost);
   if ($c_color) { $c_color='color='.$c_color."\n";}

   # Output images
   my $id=$k{'id'};
   if ($id) {
    print IO "${name}.jpg;http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=$id&type=card\n";
   } else {
    print STDERR "Missing image id for $name\n";
   }
   my $hqi=$hqim{$name};
   if ($hqi) {
    print IOH "${name}.jpg;$hqi\n";
   } else {
    print STDERR "Missing HQ image for $name\n";
   }

#TODO: value?
print OU <<EOF;
>$name
value=$rarity
${abils}rarity=$rarity
type=$type
subtype=$subtype
${c_color}converted=$c_conv
cost=$c_cost
power=$pow
toughness=$tou
timing=$timing

EOF
  }
 }
 if ($complex) {
  foreach my $q (keys %k) {
   print O "$q = ".$k{$q}."\n";
  }
  print O "\n";
 }
 %k=();
}
my $c='';
while (<F>) {
 s/\s+/ /gm;
 $c.=$_;
 if (m#</tr>#) {
   $c=~s/.*<tr/<tr/mi;
   $c=~s/\s+</</gmi;
   $c=~s/>\s+/>/gmi;
   $c=~s/\s+id="[^"<>]+"//gmi;
#  if (!/Name:/) {$c='';} else {
   if ($c=~/td colspan/) {emit();$c='--';}
   if ($c=~m#^<tr><td>([^<>]+):</td><td>(.*)</td></tr>$#) {
    my $key=$1;
    my $value=$2;
    if ($key=~/name/i && $value=~m#<a[^<>]+ href="[^"]*multiverseid=(\d+)">([^<>]+)</a>#) {
     $c="$key = $2\nID = $1";
     $k{$key}=$2;
     $k{'id'}=$1;
    } else {
     $k{$key}=$value;
     $c="$key = $value";
    }
   }
#   print O "::".$c."\n";
   $c='';
#
#  }
 }
}
emit();
close F;
close O;
close OU;
close ER;
close IO;
close IOH;
close EDI;

print STDERR "$numc cards imported\n";
print STDERR "$nume card excluded (bad edition)\n";
