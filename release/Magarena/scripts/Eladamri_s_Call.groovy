[
     new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN may search his or her library for a creature card, reveal it, and put it in his or her hand. Then shuffle his or her library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSearchIntoHandEvent(
                event,
                new MagicMayChoice(
                    "Search for a creature?",
                    MagicTargetChoice.CREATURE_CARD_FROM_LIBRARY
                    )
            ));
        }
    }
]