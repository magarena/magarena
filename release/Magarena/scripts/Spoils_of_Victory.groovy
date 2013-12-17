[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN searches his or her library for a Plains, Island, Swamp, Mountain, or Forest card and puts that card onto the battlefield. PN then shuffles his or her library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSearchOntoBattlefieldEvent(
                event,
                MagicTargetChoice.LAND_CARD_WITH_BASIC_LAND_TYPE_FROM_LIBRARY,
            ));
        }
    }
]
