[
    new MagicPermanentActivation(
        [
            MagicCondition.CAN_REGENERATE_CONDITION,
        ],
        new MagicActivationHints(MagicTiming.Pump),
        "Regen"
    ) {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [
                new MagicRemoveCounterEvent(source,MagicCounterType.PlusOne,2)
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
