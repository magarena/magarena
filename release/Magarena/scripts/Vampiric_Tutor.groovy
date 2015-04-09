[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN searches his or her library for a card, shuffle his or her library, and put that card on top of it. PN loses 2 life."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeLifeAction(event.getPlayer(),-2));
            game.addEvent(new MagicSearchToLocationEvent(
                event,
                A_CARD_FROM_LIBRARY,
                MagicLocationType.TopOfOwnersLibrary
            ));
        }
    }
]
