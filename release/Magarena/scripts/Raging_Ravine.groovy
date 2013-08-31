def CT = new MagicWhenAttacksTrigger() {
    @Override
    public MagicEvent executeTrigger(
            final MagicGame game,
            final MagicPermanent permanent,
            final MagicPermanent creature) {
        return (permanent == creature && permanent.isCreature()) ?
            new MagicEvent(
                permanent,
                this,
                "Put a +1/+1 counter on SN."
            ):
            MagicEvent.NONE;
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(new MagicChangeCountersAction(
            event.getPermanent(),
            MagicCounterType.PlusOne,
            1,
            true
        ));
    }
};

def PT = new MagicStatic(MagicLayer.SetPT, MagicStatic.UntilEOT) {
    @Override
    public void modPowerToughness(
            final MagicPermanent source,
            final MagicPermanent permanent,
            final MagicPowerToughness pt) {
        pt.set(3,3);
    }
};

def ST = new MagicStatic(MagicLayer.Type, MagicStatic.UntilEOT) {
    @Override
    public void modSubTypeFlags(
            final MagicPermanent permanent,
            final Set<MagicSubType> flags) {
        flags.add(MagicSubType.Elemental);
    }
    @Override
    public int getTypeFlags(final MagicPermanent permanent,final int flags) {
        return flags|MagicType.Creature.getMask();
    }
};

def C = new MagicStatic(MagicLayer.Color, MagicStatic.UntilEOT) {
    @Override
    public int getColorFlags(final MagicPermanent permanent,final int flags) {
        return MagicColor.Red.getMask()|MagicColor.Green.getMask();
    }
};
[
    new MagicPermanentActivation(
        [new MagicArtificialCondition(
            MagicConditionFactory.ManaCost("{1}{R}{R}{G}{G}")
        )],
        new MagicActivationHints(MagicTiming.Animate),
        "Animate"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{2}{R}{G}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Until end of turn, SN becomes a 3/3 red and green Elemental creature with " +
                "\"Whenever this creature attacks, put a +1/+1 counter on it.\" It's still a land."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent=event.getPermanent();
            game.doAction(new MagicBecomesCreatureAction(permanent,PT,ST,C));
            game.doAction(new MagicAddTurnTriggerAction(permanent,CT));
        }
    }
]
