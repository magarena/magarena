[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Each player chooses a number of lands he or she controls equal to the number of lands controlled by the player " +
                "who controls the fewest, then sacrifices the rest. Players discard cards and sacrifice creatures the same way."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPlayer player : game.getAPNAP()) {
                final int n1 = player.getNrOfPermanents(MagicType.Land) - player.getOpponent().getNrOfPermanents(MagicType.Land);
                final int n2 = player.getHandSize() - player.getOpponent().getHandSize();
                final int n3 = player.getNrOfPermanents(MagicType.Creature) - player.getOpponent().getNrOfPermanents(MagicType.Creature);
                for (int i=n1;i>0;i--) {
                    game.addEvent(new MagicSacrificePermanentEvent(event.getSource(),player,SACRIFICE_LAND));
                }
                if (n2 > 0) {
                    game.addEvent(new MagicDiscardEvent(event.getSource(),player,n2));
                }
                for (int i=n3;i>0;i--) {
                    game.addEvent(new MagicSacrificePermanentEvent(event.getSource(),player,SACRIFICE_CREATURE));
                }
            }
        }
    }
]
