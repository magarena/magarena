[
    new MagicPermanentActivation(
        [
            MagicConditionFactory.ManaCost("{B}"),
            MagicCondition.ONE_CREATURE_CONDITION,
            MagicCondition.CAN_REGENERATE_CONDITION,
            new MagicSingleActivationCondition()
        ],
        new MagicActivationHints(MagicTiming.Pump),
        "Regen"
    ) {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{B}"),
                new MagicSacrificePermanentEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.SACRIFICE_CREATURE
                )
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Regenerate SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicRegenerateAction(event.getPermanent()));
        }
    }
]
