[
    new MagicStatic(
        MagicLayer.Ability
        permanent(MagicType.Creature, Control.You)
    ) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            flags.add(MagicAbility.Unblockable);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return target.getCounters(MagicCounterType.PlusOne) > 0;
        }
    }
]

