def PT = new MagicStatic(MagicLayer.SetPT) {
    @Override
    public void modPowerToughness(
        final MagicPermanent source,
        final MagicPermanent permanent,
        final MagicPowerToughness pt) {
        pt.set(3,1);
    }
    @Override
    public boolean condition(
        final MagicGame game,
        final MagicPermanent source,
        final MagicPermanent target) {
        return target.getCounters(MagicCounterType.Feather) > 0;
    }
};

def AB = new MagicStatic(MagicLayer.Ability) {
    @Override
    public void modAbilityFlags(
        final MagicPermanent source,
        final MagicPermanent permanent,
        final Set<MagicAbility> flags) {
        permanent.addAbility(MagicAbility.Flying, flags);
    }
    @Override
    public boolean condition(
        final MagicGame game,
        final MagicPermanent source,
        final MagicPermanent target) {
        return target.getCounters(MagicCounterType.Feather) > 0;
    }
};

[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        MagicTargetChoice.TARGET_CREATURE),
                    new MagicBecomeTargetPicker(3,1),
                    this,
                    "PN may\$ put a feather counter on target creature\$."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    game.doAction(new MagicChangeCountersAction(it,MagicCounterType.Feather,1));
                    game.doAction(new MagicAddStaticAction(it, PT));
                    game.doAction(new MagicAddStaticAction(it, AB));
                });
            }
        }
    }
]
