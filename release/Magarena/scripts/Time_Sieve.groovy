[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Main),
        "Turn"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source), 
                new MagicRepeatedPermanentsEvent(
                    source, 
                    SACRIFICE_ARTIFACT, 
                    5, 
                    MagicChainEventFactory.Sac
                )
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN takes an extra turn after this one."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeExtraTurnsAction(event.getPlayer(),1));
        }
    }
]
