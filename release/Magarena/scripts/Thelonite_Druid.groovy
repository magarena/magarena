def PT = new MagicStatic(MagicLayer.SetPT, MagicStatic.UntilEOT) {
    @Override
    public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
        pt.set(2,3);
    }
};
def TY = new MagicStatic(MagicLayer.Type, MagicStatic.UntilEOT) {
    @Override
    public int getTypeFlags(final MagicPermanent permanent,final int flags) {
        return flags|MagicType.Creature.getMask();
    }
};

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Animate),
        "Animate"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayManaCostEvent(source,"{1}{G}"),
                new MagicSacrificePermanentEvent(source,SACRIFICE_CREATURE)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Forests PN controls become 2/3 creatures until end of turn. They're still lands."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> forests=
                game.filterPermanents(event.getPlayer(),FOREST_YOU_CONTROL);
            for (final MagicPermanent forest : forests) {
                game.doAction(new MagicBecomesCreatureAction(forest,PT,TY));
            }
        }
    }
]
