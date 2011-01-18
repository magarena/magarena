#!/usr/bin/perl
# Generate list of already implemented cards
# Check that all cards have image and HQ image and there is no image or HQ image without a card

use strict;
use mutils;

my %config=default_config();

my %all=();
my %cards=();
my %images=();
my %hq_images=();

# Load cards
open F,"<".$config{'cardfile'};
while (<F>) {
 next if (!/^>/);
 s/^>//;
 s/[\r\n]//gm;
 $all{$_}=1;
 $cards{$_}=1;
}
close F;

# Load images
my $current='';
open F,"<".$config{'imagefile'};
while (<F>) {
 s/[\r\n]+//gm;
 if (/^>(.*)/) {
  $current=$1;
  next;
 }
 s/\.jpg;.*$//i;
 if ($current eq 'cards') {
  $all{$_}=1;
  $images{$_}=1;
 }
 if ($current eq 'hqcards') {
  $all{$_}=1;
  $hq_images{$_}=1;
 }
}
close F;


# Save cardlist to defined file
open O,">".$config{'cardlist'};
foreach my $c (sort keys %cards) {
 print O $c."\n";
}
close O;

#Check images
foreach my $c (sort keys %all) {
 next if ($c=~/^(Forest|Island|Mountain|Plains|Swamp)2$/);
 next if ($cards{$c} && $images{$c} && $hq_images{$c});
 if (!$cards{$c}) {print "Missing card for image/HQ image $c\n";}
 if (!$images{$c} && !$hq_images{$c}) {print "Missing image and HQ image for card $c\n";}
 elsif (!$images{$c}) {print "Missing image for card $c\n";}
 elsif (!$hq_images{$c}) {print "Missing HQ image for card $c\n";}
}
