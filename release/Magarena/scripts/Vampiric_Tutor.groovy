[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN searches his or her library for a card, shuffles his or her library, and puts that card on top of it. "+
                "PN loses 2 life."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSearchToLocationEvent(
                event,
                A_CARD_FROM_LIBRARY,
                MagicLocationType.TopOfOwnersLibrary,
                false
            ));
            game.doAction(new ChangeLifeAction(event.getPlayer(),-2));
        }
    }
]
