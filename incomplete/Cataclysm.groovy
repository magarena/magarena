[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Each player chooses from among the permanents he or she controls " +
                "an artifact, a creature, an enchantment, and a land, then sacrifices the rest."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPlayer player : game.getAPNAP()) {
                final int n1 = player.getNrOfPermanents(MagicType.Artifact) - 1;
                n1.times {
                    game.addEvent(new MagicSacrificePermanentEvent(event.getSource(),player,SACRIFICE_ARTIFACT));
                }
                
                final int n2 = player.getNrOfPermanents(MagicType.Creature) - 1;
                n2.times {
                    game.addEvent(new MagicSacrificePermanentEvent(event.getSource(),player,SACRIFICE_CREATURE));
                }
                
                final int n3 = player.getNrOfPermanents(MagicType.Enchantment) - 1;
                n3.times {
                    game.addEvent(new MagicSacrificePermanentEvent(event.getSource(),player,SACRIFICE_ENCHANTMENT));
                }
                
                final int n4 = player.getNrOfPermanents(MagicType.Land) - 1;
                n4.times {
                    game.addEvent(new MagicSacrificePermanentEvent(event.getSource(),player,SACRIFICE_LAND));
                }
            }
        }
    }
]
