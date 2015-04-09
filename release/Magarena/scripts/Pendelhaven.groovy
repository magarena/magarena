[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump,true),
        "Pump"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                POS_TARGET_1_1_CREATURE,
                MagicPumpTargetPicker.create(),
                this,
                "Target 1/1 creature\$ gets +1/+2 until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicChangeTurnPTAction(it,1,2));
            });
        }
    }
]
