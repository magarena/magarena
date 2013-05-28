[
    new MagicPermanentActivation(
        [
            MagicConditionFactory.ManaCost("{3}{B}"),
            MagicCondition.ONE_CREATURE_CONDITION,
            MagicCondition.CAN_TAP_CONDITION,
        ],
        new MagicActivationHints(MagicTiming.Removal),
        "Destroy"
    ) {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostTapEvent(source,"{3}{B}"),
                new MagicSacrificePermanentEvent(
                    source,
                    MagicTargetChoice.SACRIFICE_HUMAN
                )
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_CREATURE,
                new MagicDestroyTargetPicker(false),
                this,
                "Destroy target creature\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicDestroyAction(creature));
                }
            });
        }
    }
]
