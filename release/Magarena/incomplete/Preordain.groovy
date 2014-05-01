[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Scry 2. " +
                "PN draws a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addFirstEvent(new MagicScryXEvent(event.getSource(),event.getPlayer(),2));
            game.doAction(new MagicDrawAction(event.getPlayer()));
        }
    }
]
