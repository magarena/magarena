[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Except for the player with the fewest lands, each player sacrifices lands until all players control the same number of lands. " +
                "Players discard cards and sacrifice creatures the same way."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPlayer player : game.getAPNAP()) {
                final int n1 = player.getNrOfPermanents(MagicType.Land) - player.getOpponent().getNrOfPermanents(MagicType.Land);
                n1.times {
                    game.addEvent(new MagicSacrificePermanentEvent(event.getSource(),player,SACRIFICE_LAND));
                }
                
                final int n2 = player.getHandSize() - player.getOpponent().getHandSize();
                if (n2 > 0) {
                    game.addEvent(new MagicDiscardEvent(event.getSource(),player,n2));
                }
                
                final int n3 = player.getNrOfPermanents(MagicType.Creature) - player.getOpponent().getNrOfPermanents(MagicType.Creature);
                n3.times {
                    game.addEvent(new MagicSacrificePermanentEvent(event.getSource(),player,SACRIFICE_CREATURE));
                }
            }
        }
    }
]
