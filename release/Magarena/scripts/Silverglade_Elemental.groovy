[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {     
            return new MagicEvent(
                permanent,
                this,
                "PN may search his or her library for a Forest card and put that card onto the battlefield, then shuffle PN's library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSearchOntoBattlefieldEvent(
                event,
                new MagicMayChoice(
                    "Search for a forest card?",
                    MagicTargetChoice.FOREST_CARD_FROM_LIBRARY
                ),
                MagicPlayMod.unTAPPED
            ));
        }
    }
]
