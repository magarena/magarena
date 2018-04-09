def NONLAND_PERMANENT_WITHOUT_FATE_COUNTER = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
        return !target.isLand() && target.getCounters(MagicCounterType.Fade) == 0;
    }
};

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Destroy"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{5}"),
                new MagicTapEvent(source),
                new MagicSacrificeEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Destroy each nonland permanent without a fate counter on it, "+
                "then remove all fate counters from all permanents."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DestroyAction(NONLAND_PERMANENT_WITHOUT_FATE_COUNTER.filter(event)));
            PERMANENT.filter(event) each {
                game.doAction(new ChangeCountersAction(
                    event.getPlayer(),
                    it,
                    MagicCounterType.Fate,
                    -it.getCounters(MagicCounterType.Fate)
                ));
            }
        }
    }
]
