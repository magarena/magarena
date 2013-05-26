[
    new MagicPermanentActivation(
        [
            MagicCondition.CAN_TAP_CONDITION,
            MagicConditionFactory.ManaCost("{1}")
        ],
        new MagicActivationHints(MagicTiming.Tapping),
        "Tap"
    ) {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostTapEvent(source,"{1}")];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_ARTIFACT_OR_CREATURE_OR_LAND,
                new MagicTapTargetPicker(true,false),
                this,
                "Tap target artifact, creature, or land\$."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent permanent) {
                    game.doAction(new MagicTapAction(permanent,true));
                }
            });
        }
    }
]
