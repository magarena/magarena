// becomes a 2/2 Kithkin Spirit
def PT1 = new MagicStatic(MagicLayer.SetPT) {
    @Override
    public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
        pt.set(2,2);
    }
};
def ST1 = new MagicStatic(MagicLayer.Type) {
    @Override
    public void modSubTypeFlags(final MagicPermanent permanent,final Set<MagicSubType> flags) {
        flags.clear();
        flags.add(MagicSubType.Kithkin);
        flags.add(MagicSubType.Spirit);
    }
};

// becomes a 4/4 Kithkin Spirit Warrior
def PT2 = new MagicStatic(MagicLayer.SetPT) {
    @Override
    public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
        pt.set(4,4);
    }
};
def ST2 = new MagicStatic(MagicLayer.Type) {
    @Override
    public void modSubTypeFlags(final MagicPermanent permanent,final Set<MagicSubType> flags) {
        flags.clear();
        flags.add(MagicSubType.Kithkin);
        flags.add(MagicSubType.Spirit);
        flags.add(MagicSubType.Warrior);
    }
};

// becomes a 8/8 Kithkin Spirit Warrior Avatar with flying and first strike.
def PT3 = new MagicStatic(MagicLayer.SetPT) {
    @Override
    public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
        pt.set(8,8);
    }
};
def ST3 = new MagicStatic(MagicLayer.Type) {
    @Override
    public void modSubTypeFlags(final MagicPermanent permanent,final Set<MagicSubType> flags) {
        flags.clear();
        flags.add(MagicSubType.Kithkin);
        flags.add(MagicSubType.Spirit);
        flags.add(MagicSubType.Warrior);
        flags.add(MagicSubType.Avatar);
    }
};
def AB3 = new MagicStatic(MagicLayer.Ability) {
    @Override
    public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
        permanent.addAbility(MagicAbility.Flying, flags);
        permanent.addAbility(MagicAbility.FirstStrike, flags);
    }
};

[
    new MagicPermanentActivation(
        [
            MagicCondition.NOT_EXCLUDE_COMBAT_CONDITION,
            MagicConditionFactory.NotSubType(MagicSubType.Spirit)
        ],
        new MagicActivationHints(MagicTiming.Animate),
        "Spirit"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{R/W}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "SN becomes a 2/2 Kithkin Spirit."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new BecomesCreatureAction(event.getPermanent(),PT1,ST1));
        }
    },
    new MagicPermanentActivation(
        [
            MagicCondition.NOT_EXCLUDE_COMBAT_CONDITION,
            MagicConditionFactory.HasSubType(MagicSubType.Spirit),
            MagicConditionFactory.NotSubType(MagicSubType.Warrior)
        ],
        new MagicActivationHints(MagicTiming.Animate),
        "Warrior"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{R/W}{R/W}{R/W}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "SN becomes a 4/4 Kithkin Spirit Warrior."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getPermanent().hasSubType(MagicSubType.Spirit)) {
                game.doAction(new BecomesCreatureAction(event.getPermanent(),PT2,ST2));
            }
        }
    },
    new MagicPermanentActivation(
        [
            MagicCondition.NOT_EXCLUDE_COMBAT_CONDITION,
            MagicConditionFactory.HasSubType(MagicSubType.Warrior),
            MagicConditionFactory.NotSubType(MagicSubType.Avatar)
        ],
        new MagicActivationHints(MagicTiming.Animate),
        "Avatar"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{R/W}{R/W}{R/W}{R/W}{R/W}{R/W}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "SN becomes a 8/8 Kithkin Spirit Warrior Avatar with flying and first strike."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getPermanent().hasSubType(MagicSubType.Warrior)) {
                game.doAction(new BecomesCreatureAction(event.getPermanent(),PT3,ST3,AB3));
            }
        }
    }
]
