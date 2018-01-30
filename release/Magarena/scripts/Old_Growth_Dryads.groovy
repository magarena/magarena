[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                permanent.getOpponent(),
                new MagicMayChoice("Search your library for a basic land card?"),
                this,
                "PN may search PN's library for a basic land card, put it onto the battlefield tapped, than shuffle PN's library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.addEvent(new MagicSearchOntoBattlefieldEvent(
                    event,
                    new MagicFromCardFilterChoice(
                        BASIC_LAND_CARD_FROM_LIBRARY,
                        1,
                        false,
                        "a basic land card from your library"
                    ),
                    MagicPlayMod.TAPPED) {
            }
        }
    }
]

