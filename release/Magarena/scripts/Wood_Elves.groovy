[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {     
            return new MagicEvent(
                permanent,
                this,
                "PN may searches PN's library for a Forest card and puts that card onto the battlefield, then shuffles PN's library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSearchOntoBattlefieldEvent(event, MagicTargetChoice.FOREST_CARD_FROM_LIBRARY));
        }
    }
]
