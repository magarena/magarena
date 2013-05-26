[
    new MagicPermanentActivation(
        [
            MagicConditionFactory.ManaCost("{R}"),
            MagicCondition.ONE_CREATURE_CONDITION,
            MagicCondition.CONTROL_ARTIFACT_CONDITION
        ],
        new MagicActivationHints(MagicTiming.Pump),
        "Pump"
    ) {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            final MagicPlayer player = source.getController();
            return [
                new MagicPayManaCostEvent(source,"{R}"),
                new MagicSacrificePermanentEvent(
                    source,
                    player,
                    MagicTargetChoice.SACRIFICE_ARTIFACT
                )
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "SN gets +3/+0 until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeTurnPTAction(event.getPermanent(),3,0));
        }
    }
]
