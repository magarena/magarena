def NONLAND_PERMANENT_WITHOUT_FATE_COUNTER = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
        return !target.isLand() && target.getCounters(MagicCounterType.Fade) == 0;
    } 
};

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Destroy"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
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
            final Collection<MagicPermanent> targets = 
            game.filterPermanents(NONLAND_PERMANENT_WITHOUT_FATE_COUNTER);
                game.doAction(new MagicDestroyAction(targets));
                final List<MagicPermanent> permanents = game.filterPermanents(PERMANENT);
                for (final MagicPermanent permanent : permanents) {   
                    game.doAction(new ChangeCountersAction(
                        permanent,
                        MagicCounterType.Fate,
                        -permanent.getCounters(MagicCounterType.Fate)
                    )); 
            }   
        }
    }
]
