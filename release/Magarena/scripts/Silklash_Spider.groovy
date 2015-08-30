[
    new MagicPermanentActivation(
        [new MagicArtificialCondition(MagicConditionFactory.OpponentControl(CREATURE_WITH_FLYING))],
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source, "{X}{G}{G}")];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                payedCost.getX(),
                this,
                "SN deals X damage to each creature with flying. (X=${payedCost.getX()})"
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPermanent permanent : CREATURE_WITH_FLYING.filter(event)) {
                game.doAction(new DealDamageAction(event.getPermanent(), permanent, event.getRefInt()));
            }
        }
    }
]
