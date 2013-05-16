[
    new MagicPermanentActivation(
        [
            MagicCondition.ABILITY_ONCE_CONDITION,
            MagicConditionFactory.ManaCost("{3}{G}")
        ],
        new MagicActivationHints(MagicTiming.Pump),
        "Pump") {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.create("{3}{G}")),
                new MagicPlayAbilityEvent(source)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "SN gets +3/+3 until end of turn."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            game.doAction(new MagicChangeTurnPTAction(event.getPermanent(),3,3));
        }
    }
]
