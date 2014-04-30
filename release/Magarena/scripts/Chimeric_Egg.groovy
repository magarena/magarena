def PT = new MagicStatic(MagicLayer.SetPT, MagicStatic.UntilEOT) {
    @Override
    public void modPowerToughness(
        final MagicPermanent source,
        final MagicPermanent permanent,
        final MagicPowerToughness pt) {
            pt.set(6,6);
        }
};

def ST = new MagicStatic(MagicLayer.Type, MagicStatic.UntilEOT) {
    @Override
    public void modSubTypeFlags(
            final MagicPermanent permanent,
            final Set<MagicSubType> flags) {
        flags.add(MagicSubType.Construct);
    }
    @Override
    public int getTypeFlags(final MagicPermanent permanent,final int flags) {
        return flags|MagicType.Creature.getMask();
    }
};

def AB = new MagicStatic(MagicLayer.Ability, MagicStatic.UntilEOT) {
    @Override
    public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
        permanent.addAbility(MagicAbility.Trample, flags);
    }
};
[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Animate,1),
        "Animate"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicRemoveCounterEvent(source,MagicCounterType.Charge,3),
                new MagicPlayAbilityEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "SN becomes a 6/6 Construct artifact creature with trample until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicBecomesCreatureAction(event.getPermanent(),PT,AB,ST));
        }
    }
]
