[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Draw 4 cards, then choose X cards from your hand and discard the rest."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.doAction(new MagicDrawAction(event.getPlayer(), 4));
        final int amount = event.getPlayer().getHandSize() - event.getCardOnStack().getX();
                game.addEvent(new MagicDiscardEvent(event.getSource(),event.getPlayer(),amount));
        }
    }
]
