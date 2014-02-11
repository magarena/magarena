[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN searches his or her library for an artifact card, reveals it, " + 
                "puts it into his or her hand, then shuffles his or her library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSearchIntoHandEvent(
                event,
                MagicTargetChoice.ARTIFACT_CARD_FROM_LIBRARY
            ));
        }
    }
]
