[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Pump"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source), new MagicPayManaCostEvent(source, "{X}")];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            final int amount=payedCost.getX();
            return new MagicEvent(
                source,
                amount,
                this,
                "PN removes all +1/+1 counters from SN, then puts "+amount+" +1/+1 counters on SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            int oldCounters=0;
            oldCounters-=event.getPermanent().getCounters(MagicCounterType.PlusOne);
            game.doAction(new ChangeCountersAction(event.getPermanent(),MagicCounterType.PlusOne,oldCounters));
            game.doAction(new ChangeCountersAction(event.getPermanent(),MagicCounterType.PlusOne,event.getRefInt()));
        }
    }
]
