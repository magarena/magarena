def PT1 = new MagicStatic(MagicLayer.SetPT) {
    @Override
    public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
        pt.set(3,3);
    }
};

def ST1 = new MagicStatic(MagicLayer.Type) {
    @Override
    public void modSubTypeFlags(final MagicPermanent permanent,final Set<MagicSubType> flags) {
        flags.clear();
        flags.add(MagicSubType.Human);
        flags.add(MagicSubType.Warrior);
    }
};

def ST2 = new MagicStatic(MagicLayer.Type) {
    @Override
    public void modSubTypeFlags(final MagicPermanent permanent,final Set<MagicSubType> flags) {
        flags.clear();
        flags.add(MagicSubType.Human);
        flags.add(MagicSubType.Spirit);
        flags.add(MagicSubType.Warrior);
    }
};

def AB2 = new MagicStatic(MagicLayer.Ability) {
    @Override
    public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
        permanent.addAbility(MagicAbility.Trample, flags);
        permanent.addAbility(MagicAbility.Lifelink, flags);
    }
};

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Animate),
        "Warrior"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{1}{W/B}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "SN becomes a Human Warrior with base power and toughness 3/3."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new BecomesCreatureAction(event.getPermanent(),PT1,ST1));
        }
    },
    new MagicPermanentActivation(
        [
            MagicConditionFactory.HasSubType(MagicSubType.Warrior)
        ],
        new MagicActivationHints(MagicTiming.Animate),
        "Spirit"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{2}{W/B}{W/B}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "SN becomes a Human Spirit Warrior with trample and lifelink."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getPermanent().hasSubType(MagicSubType.Warrior)) {
                game.doAction(new BecomesCreatureAction(event.getPermanent(),ST2,AB2));
            }
        }
    },
    new MagicPermanentActivation(
        [
            MagicConditionFactory.HasSubType(MagicSubType.Spirit)
        ],
        new MagicActivationHints(MagicTiming.Pump),
        "Counters"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{3}{W/B}{W/B}{W/B}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Put five +1/+1 counters on SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getPermanent().hasSubType(MagicSubType.Spirit)) {
                game.doAction(new ChangeCountersAction(event.getPlayer(),event.getPermanent(),MagicCounterType.PlusOne,5));
            }
        }
    }
]
