[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {      
            return new MagicEvent(
                permanent,
                this,
                "PN may search his or her library for a basic land card and put that card onto the battlefield tapped, then shuffle his or her library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSearchOntoBattlefieldEvent(
                event,
                new MagicMayChoice(
                    "Search for a basic land card?",
                    MagicTargetChoice.BASIC_LAND_CARD_FROM_LIBRARY
                ),
                MagicPlayMod.TAPPED
            ));
        }
    }
]
