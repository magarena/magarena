[
    new MagicTapCreatureActivation(
        [
            MagicCondition.CAN_TAP_CONDITION,
            MagicConditionFactory.ManaCost("{U}")
        ],
        new MagicActivationHints(MagicTiming.Tapping),
        "Tap"
    ) {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostTapEvent(source,"{U}")
            ];
        }
    },
    new MagicPermanentActivation(
        [
            MagicCondition.CAN_TAP_CONDITION,
            MagicConditionFactory.ManaCost("{U}")
        ],
        new MagicActivationHints(MagicTiming.Tapping),
        "Untap"
    ) {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostTapEvent(source,"{U}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.POS_TARGET_CREATURE,
                new MagicTapTargetPicker(false,true),
                this,
                "Untap target creature\$."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicUntapAction(creature));
                }
            });
        }
    }
]
