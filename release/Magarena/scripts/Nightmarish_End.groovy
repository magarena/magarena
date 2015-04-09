[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int X = cardOnStack.getController().getHandSize();
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                new MagicWeakenTargetPicker(-X, -X),
                this,
                "Target creature\$ gets -X/-X until end of turn, where X is the number of cards in PN's hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final int X = event.getPlayer().getHandSize();
                game.doAction(new MagicChangeTurnPTAction(it, -X, -X));
            });
        }
    }
]
