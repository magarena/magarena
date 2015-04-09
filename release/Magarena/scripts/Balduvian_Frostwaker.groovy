def PT = new MagicStatic(MagicLayer.SetPT, MagicStatic.Forever) {
    @Override
    public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
        pt.set(2,2);
    }
};
def C = new MagicStatic(MagicLayer.Color, MagicStatic.Forever) {
    @Override
    public int getColorFlags(final MagicPermanent permanent,final int flags) {
        return MagicColor.Blue.getMask();
    }
};
def ST = new MagicStatic(MagicLayer.Type, MagicStatic.Forever) {
    @Override
    public void modSubTypeFlags(final MagicPermanent permanent,final Set<MagicSubType> flags) {
        flags.add(MagicSubType.Elemental);
    }
    @Override
    public int getTypeFlags(final MagicPermanent permanent,final int flags) {
        return flags|MagicType.Creature.getMask();
    }
};
def AB = new MagicStatic(MagicLayer.Ability, MagicStatic.Forever) {
    @Override
    public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
        permanent.addAbility(MagicAbility.Flying, flags);
    }
};

def choice = Positive("target snow land");

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Animate),
        "Animate"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source), new MagicPayManaCostEvent(source, "{U}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                choice,
                this,
                "Target snow land\$ becomes a 2/2 blue Elemental creature with flying. It's still a land."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicBecomesCreatureAction(it,PT,AB,ST,C));
            });
        }
    }
]
