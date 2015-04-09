[
    new MagicStatic(
        MagicLayer.Ability,
        CREATURE) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            permanent.addAbility(MagicAbility.Lifelink, flags);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source.getEquippedCreature().hasSubType(MagicSubType.Human) &&
                   target == source.getEquippedCreature();
        }
    }
]
