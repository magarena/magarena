def PT = new MagicStatic(MagicLayer.SetPT, MagicStatic.UntilEOT) {
    @Override
    public void modPowerToughness(
            final MagicPermanent source,
            final MagicPermanent permanent,
            final MagicPowerToughness pt) {
        final int charge=permanent.getCounters(MagicCounterType.Charge);
        pt.set(charge,charge);
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
[
    new MagicPermanentActivation(
        [MagicCondition.NOT_EXCLUDE_COMBAT_CONDITION],
        new MagicActivationHints(MagicTiming.Animate,1),
        "Becomes"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{1}"),
                new MagicPlayAbilityEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Until end of turn, SN becomes a Construct artifact creature with " +
                "\"This creature's power and toughness are each equal to the number of charge counters on it.\""
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicBecomesCreatureAction(event.getPermanent(),PT,ST));
        }
    }
]
