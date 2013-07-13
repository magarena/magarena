[
    new MagicStatic(
        MagicLayer.Ability,
        MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL
    ) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            permanent.addTrigger(MagicExaltedTrigger.create());
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source != target;
        }
    }
]
