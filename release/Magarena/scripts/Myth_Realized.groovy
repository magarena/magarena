def PT = new MagicStatic(MagicLayer.SetPT, MagicStatic.UntilEOT) {
    @Override
    public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
        final int amount=permanent.getCounters(MagicCounterType.Lore);
        pt.set(amount,amount);
    }
};

def ST = new MagicStatic(MagicLayer.Type, MagicStatic.UntilEOT) {
    @Override
    public void modSubTypeFlags(final MagicPermanent permanent, final Set<MagicSubType> flags) {
        flags.add(MagicSubType.Monk);
        flags.add(MagicSubType.Avatar);
    }
    @Override
    public int getTypeFlags(final MagicPermanent permanent,final int flags) {
        return flags|MagicType.Creature.getMask();
    }
};
[
    new MagicPermanentActivation(
        [
            MagicCondition.NOT_EXCLUDE_COMBAT_CONDITION,
            new MagicArtificialCondition(MagicConditionFactory.CounterAtLeast(MagicCounterType.Lore, 1))
        ],
        new MagicActivationHints(MagicTiming.Animate),
        "Becomes"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{W}"),
                new MagicPlayAbilityEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Until end of turn, SN becomes a Monk Avatar creature in addition to its other types and gains "+
                "\"This creature's power and toughness are each equal to the number of lore counters on it.\""
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new BecomesCreatureAction(event.getPermanent(),PT,ST));
        }
    }
]
