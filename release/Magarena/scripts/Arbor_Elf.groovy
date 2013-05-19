[
    new MagicPermanentActivation(
        [MagicCondition.CAN_TAP_CONDITION],
        new MagicActivationHints(MagicTiming.Tapping),
        "Untap"
    ) {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.POS_TARGET_FOREST,
                new MagicTapTargetPicker(false,true),
                this,
                "Untap target Forest\$."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent forest) {
                    game.doAction(new MagicUntapAction(forest));
                }
            });
        }
    }
]
