[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN searches his or her library for three cards, exiles them, then shuffles his or her library." +
                "PN draws a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSearchToLocationEvent(
                event,
                new MagicFromCardFilterChoice(CARD_FROM_LIBRARY, 3, true, "to be exiled"),
                MagicLocationType.Exile
            ));
            game.doAction(new DrawAction(event.getPlayer()));
        }
    }
]
