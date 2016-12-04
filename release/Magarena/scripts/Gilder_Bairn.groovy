def PERMANENT_COUNTERS = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
        return target.hasCounters();
    }
};

def TARGET_PERMANENT_WITH_COUNTERS = new MagicTargetChoice(
    PERMANENT_COUNTERS,
    "target permanent with counters"
)

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "+Counter"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{2}{G/U}"),
                new MagicUntapEvent(source)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_PERMANENT_WITH_COUNTERS,
                this,
                "Double the number of each kind of counter on target permanent\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                for (final MagicCounterType counterType : MagicCounterType.values()) {
                    if (it.hasCounters(counterType)) {
                        game.doAction(new ChangeCountersAction(
                            it,
                            counterType,
                            it.getCounters(counterType)
                        ));
                    }
                }
            });
        }
    }
]
//AI relucatant/hardly ever uses untap ability
