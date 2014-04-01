[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Flash),
        "Land"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN may put a land card from PN's hand onto the battlefield."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicPutOntoBattlefieldEvent(
                event,
                new MagicMayChoice(
                    "Put a land card onto the battlefield?",
                    MagicTargetChoice.LAND_CARD_FROM_HAND
                )
            ));
        }
    }
]
