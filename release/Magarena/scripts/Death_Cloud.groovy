[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Each player loses X life, discards X cards, sacrifices X creatures, then sacrifices X lands."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getCardOnStack().getX();
            for (final MagicPlayer player : game.getAPNAP()) {
                game.doAction(new ChangeLifeAction(player,-amount));
            }
            for (final MagicPlayer player : game.getAPNAP()) {
                game.addEvent(new MagicDiscardEvent(event.getSource(),player,amount));
            }
            for (final MagicPlayer player : game.getAPNAP()) {
                for (final int count = 1; count <= amount; count++) {
                    game.addEvent(new MagicSacrificePermanentEvent(event.getSource(),player,SACRIFICE_CREATURE));
                }
            }
            for (final MagicPlayer player : game.getAPNAP()) {
                for (final int count = 1; count <= amount; count++) {
                    game.addEvent(new MagicSacrificePermanentEvent(event.getSource(),player,SACRIFICE_LAND));
                }
            }
        }
    }
]
// All actions and events should be simultaneous for each player after choices made
