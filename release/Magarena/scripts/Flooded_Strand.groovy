[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Land),
        "Search"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayLifeEvent(source,1),
                new MagicSacrificeEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN searches his or her library for a Plains or Island card and put it onto the battlefield. Then shuffle PN's library."
            );
        }

        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            game.addEvent(new MagicSearchOntoBattlefieldEvent(
                event,
                MagicTargetChoice.PLAINS_OR_ISLAND_CARD_FROM_LIBRARY
            ));
        }
    }
]
