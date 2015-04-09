def PT = new MagicStatic(MagicLayer.SetPT, MagicStatic.UntilEOT) {
    @Override
    public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
        pt.set(4,4);
    }
};
def ST = new MagicStatic(MagicLayer.Type, MagicStatic.UntilEOT) {
    @Override
    public int getTypeFlags(final MagicPermanent permanent,final int flags) {
        return flags|MagicType.Creature.getMask();
    }
};
def SU = new MagicStatic(MagicLayer.Type, MagicStatic.UntilEOT) {
    @Override
    public void modSubTypeFlags(final MagicPermanent permanent, final Set<MagicSubType> flags) {
        flags.add(MagicSubType.Elemental);
    }
};
[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Animate),
        "Animate"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source,"{1}{R}{G}")];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_LAND,
                this,
                "Target land\$ becomes a 4/4 Elemental creature until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicBecomesCreatureAction(it,PT,ST,SU));
            });
        }
    }
]
