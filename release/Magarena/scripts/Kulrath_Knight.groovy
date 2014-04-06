[
    new MagicStatic(
        MagicLayer.Ability,
        MagicTargetFilterFactory.TARGET_CREATURE_YOUR_OPPONENT_CONTROLS) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            permanent.addAbility(MagicAbility.CannotAttackOrBlock, flags);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return target.hasCounters();
        }
    }
]
