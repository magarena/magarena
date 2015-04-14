[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN draws 4 cards, then keep X cards in his or her hand and discard the rest."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DrawAction(event.getPlayer(), 4));
            final int amount = event.getPlayer().getHandSize() - event.getCardOnStack().getX();
            if (amount > 0) {
                game.addEvent(new MagicDiscardEvent(event.getSource(),event.getPlayer(),amount));
            }
        }
    }
]
