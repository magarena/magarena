[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Each player draw three cards then discard three cards at random."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPlayer player : game.getAPNAP()) {
                game.doAction(new DrawAction(player, 3));
            }
            for (final MagicPlayer player : game.getAPNAP()) {
                game.addEvent(MagicDiscardEvent.Random(event.getSource(),player,3));
            }
        }
    }
]
