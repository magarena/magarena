def ARTIFACT_CREATURE_OR_LAND_COUNTERS = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
        return target.hasCounters() &&
                (target.hasType(MagicType.Artifact) || target.hasType(MagicType.Creature) || target.hasType(MagicType.Land));
    }
};

def TARGET_ARTIFACT_CREATURE_OR_LAND_WITH_COUNTERS = new MagicTargetChoice(
    ARTIFACT_CREATURE_OR_LAND_COUNTERS,
    "target artifact, creature, or land with counters"
)

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "+Counter"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayManaCostEvent(source, "{G}{U}")
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_ARTIFACT_CREATURE_OR_LAND_WITH_COUNTERS,
                this,
                "For each counter on target artifact, creature, or land, PN puts another of those counters on that permanent."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                for (final MagicCounterType counterType : MagicCounterType.values()) {
                    if (it.hasCounters(counterType)) {
                        game.doAction(new ChangeCountersAction(
                            event.getPlayer(),
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
