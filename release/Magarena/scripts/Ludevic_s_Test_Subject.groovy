[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "+Counter"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{1}{U}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN puts a hatchling counter on SN. Then if there are five or more hatchling counters on it, "+
                "remove all of them and transform SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent();
            game.doAction(new ChangeCountersAction(event.getPlayer(),permanent,MagicCounterType.Hatchling,1));
            final int counters = permanent.getCounters(MagicCounterType.Hatchling);
            if (counters >= 5) {
                game.doAction(new ChangeCountersAction(event.getPlayer(),permanent,MagicCounterType.Hatchling,-counters));
                game.doAction(new TransformAction(permanent));
            }
        }
    }
]
