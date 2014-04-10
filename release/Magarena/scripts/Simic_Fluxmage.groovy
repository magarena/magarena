def PLUSONE_CONDITION = new MagicCondition() {
    public boolean accept(final MagicSource source) {
        MagicPermanent permanent = (MagicPermanent)source;
        return permanent.getCounters(MagicCounterType.PlusOne) > 0;
    }
};
[
    new MagicPermanentActivation(
        [PLUSONE_CONDITION],
        new MagicActivationHints(MagicTiming.Main),
        "Move"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayManaCostEvent(source,"{1}{U}")
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.TARGET_CREATURE,
                MagicPumpTargetPicker.create(),
                this,
                "PN moves a +1/+1 counter from SN onto target\$ creature."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent creature ->
                game.doAction(new MagicChangeCountersAction(event.getPermanent(),MagicCounterType.PlusOne,-1,true));
                game.doAction(new MagicChangeCountersAction(creature,MagicCounterType.PlusOne,1,true));
            });
        }
    }
]
