def PT = new MagicStatic(MagicLayer.SetPT) {
    @Override
    public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
        pt.set(3,1);
    }
    @Override
    public boolean condition(final MagicGame game, final MagicPermanent source, final MagicPermanent target) {
        return target.hasCounters(MagicCounterType.Feather);
    }
};

def AB = new MagicStatic(MagicLayer.Ability) {
    @Override
    public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
        permanent.addAbility(MagicAbility.Flying, flags);
    }
    @Override
    public boolean condition(final MagicGame game, final MagicPermanent source, final MagicPermanent target) {
        return target.hasCounters(MagicCounterType.Feather);
    }
};

[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    TARGET_CREATURE
                ),
                new MagicBecomeTargetPicker(3,1,true),
                this,
                "PN may\$ put a feather counter on target creature\$."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    game.doAction(new ChangeCountersAction(event.getPlayer(),it,MagicCounterType.Feather,1));
                    game.doAction(new AddStaticAction(it, PT));
                    game.doAction(new AddStaticAction(it, AB));
                });
            }
        }
    }
]
