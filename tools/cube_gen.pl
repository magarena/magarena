#!/usr/bin/perl
# Generate automatically generated cubes based on specified config

use strict;
use mutils;

my %config=default_config();

# Prefix (output directory + possible prefix) for generated cubes
my $cube_output_prefix=$config{'root'}.'resources/magic/data/';

my $cubename='';
my %ban=();
my %allow_all=();
my %allow=();
my %allow_ed=();
my %restrict=();
my %avail=();

# Output only available cards
sub load_cards {
 my $filename=shift;
 open P,"<$filename";
 while (<P>) {
  s/[\r\n]+//gm;
  $avail{$_}=1;
 }
 close P;
}

load_cards($config{'cardlist'});

#Load edition contents
my @ed=();
open EDI,"<".$config{'edition_contents'};
while (<EDI>) {
 s/[\r\n]+//gm;
 push @ed,$_;
}
close EDI;

# Write out a signle cube from data in memory
sub emit {
 if ($cubename eq '') {return ;}
 my %out=();
 foreach my $r (@ed) {
  my ($card,$edition)=split(/\|/,$r);
  next if ($ban{$card});
  if ($allow_all{$card}) {$out{$card}=1;next;};
  if ($allow{$card}) {$out{$card}=1;next;};
  if ($allow_ed{$edition} || $allow_ed{'*'}) {$out{$card}=1;next;};
 }
 print STDERR "Generating cube $cubename\n";
 open Q,">".$cube_output_prefix.$cubename."_cube.txt";
 foreach my $card (sort keys %out) {
  next if (!$avail{$card});
  print Q "$card\n";
 }
 close Q;
 %ban=();
 %allow=();
 %allow_ed=();
 %restrict=();
 $cubename='';
}

# If commandline parameter is specified, use it as source of cube data
if ($ARGV[0]) {
 $config{'cubegen_data'}=$ARGV[0]
}

# Load cubegen data
open F,"<".$config{'cubegen_data'};
while (<F>) {
 next if (/^#/);
 s/[\r\n]+//gm;
 s/#.*//;
 s/^\s+//gm;
 s/\s+$//gm;
 next if (/^$/);
 if (/^\[(.*)\]/) {
  my $nextcube=$1;
  emit();
  $cubename=$nextcube;
 } elsif(/^disallow:(.*)/) {
  $ban{$1}=1;
 } elsif(/^allow:(.*)/) {
  $allow{$1}=1;
 } elsif(/^universal:(.*)/) {
  $allow_all{$1}=1;
 } elsif(/^restrict:(.*)/) {
  $restrict{$1}=1;
 } else {
  $allow_ed{$_}=1;
 }
}
emit();
close F;
