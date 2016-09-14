def POWER_4_OR_GREATER_CONDITION = new MagicCondition() {
    public boolean accept(final MagicSource source) {
        final MagicPermanent permanent = (MagicPermanent)source;
        return permanent.getPower() >= 4;
    }
};

[
    new MagicPermanentActivation(
        [
            POWER_4_OR_GREATER_CONDITION
        ],
        new MagicActivationHints(MagicTiming.Pump),
        "Pump"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                POS_TARGET_CREATURE,
                MagicPumpTargetPicker.create(),
                this,
                "Target creature\$ gets +4/+4 until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new ChangeTurnPTAction(it,4,4));
            });
        }
    }
]
