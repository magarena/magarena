[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {     
            return new MagicEvent(
                permanent,
                this,
                "PN may search his or her library for a basic land card, reveal it, put it into his or her hand, and shuffle his or her library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSearchIntoHandEvent(
                event,
                new MagicMayChoice(
                    "Search for a basic land card?",
                    MagicTargetChoice.BASIC_LAND_CARD_FROM_LIBRARY
                )
            ));
        }
    }
]
