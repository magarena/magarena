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
        new MagicActivationHints(MagicTiming.Removal),
        "Remove"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicSacrificeEvent(source)];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_PERMANENT_WITH_COUNTERS,
                MagicCountersTargetPicker.create(),
                this,
                "Remove all counters from target permanent\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                for (final MagicCounterType counterType : it.getCounterTypes()) {
                    game.doAction(new ChangeCountersAction(event.getPlayer(),it,counterType,-it.getCounters(counterType)));
                }
            });
        }
    }
]
