//basic subtype is Kithkin
def ST = new MagicStatic(MagicLayer.Type) {
    @Override
    public void modSubTypeFlags(final MagicPermanent permanent,final Set<MagicSubType> flags) {
        flags.removeAll(MagicSubType.ALL_CREATURES);
        flags.add(MagicSubType.Kithkin);
    }
};
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
        flags.add(MagicSubType.Avatar);
    }
};
def AB1 = new MagicStatic(MagicLayer.Ability) {
    @Override
    public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
        flags.add(MagicAbility.FirstStrike);
    }
};
def AB2 = new MagicStatic(MagicLayer.Ability) {
    @Override
    public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
        flags.add(MagicAbility.Flying);
    }
};

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Animate,1),
        "Spirit"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{R/W}"),
                new MagicPlayAbilityEvent(source)
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
            game.doAction(new MagicBecomesCreatureAction(event.getPermanent(),PT1,ST,ST1));
        }
    },
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Animate,1),
        "Warrior"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{R/W}{R/W}{R/W}"),
                new MagicPlayAbilityEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return (source.hasSubType(MagicSubType.Spirit))?
            new MagicEvent(
                source,
                this,
                "SN becomes a 4/4 Kithkin Spirit Warrior."
            ):
            MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicBecomesCreatureAction(event.getPermanent(),PT2,ST,ST1,ST2));
        }
    },
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Animate,1),
        "Avatar"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{R/W}{R/W}{R/W}{R/W}{R/W}{R/W}"),
                new MagicPlayAbilityEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return (source.hasSubType(MagicSubType.Warrior))?
            new MagicEvent(
                source,
                this,
                "SN becomes a 8/8 Kithkin Spirit Warrior Avatar with flying and first strike."
            ):
            MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicBecomesCreatureAction(event.getPermanent(),PT3,AB1,AB2,ST,ST1,ST2,ST3));
        }
    }
]
