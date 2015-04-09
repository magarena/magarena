[
    new MagicStatic(
        MagicLayer.Ability,
        CREATURE) {
        @Override
        public void modAbilityFlags(
                final MagicPermanent source,
                final MagicPermanent permanent,
                final Set<MagicAbility> flags) {
            permanent.addAbility(MagicAbility.Vigilance, flags);
        }
        @Override
        public boolean condition(
                final MagicGame game,
                final MagicPermanent source,
                final MagicPermanent target) {
            return target == source.getEquippedCreature() &&
                   (source.getEquippedCreature().hasSubType(MagicSubType.Human) ||
                    source.getEquippedCreature().hasSubType(MagicSubType.Angel));
        }
    }
]
