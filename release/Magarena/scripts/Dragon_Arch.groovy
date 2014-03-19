[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Token),
        "Put"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayManaCostEvent(source,"{2}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN may put a multicolored creature card from his or her hand onto the battlefield."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicPutOntoBattlefieldEvent(
                event,
                new MagicMayChoice(
                    "Put a multicolored creature card onto the battlefield?",
                    MagicTargetChoice.MULTICOLORED_CREATURE_CARD_FROM_HAND
                )
            ));
        }
    }
]
