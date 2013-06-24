[
    new MagicStatic(
        MagicLayer.Ability,
        MagicTargetFilter.TARGET_CREATURE) {
        @Override
        public void modAbilityFlags(
                final MagicPermanent source,
                final MagicPermanent permanent,
                final Set<MagicAbility> flags) {
            flags.add(MagicAbility.Vigilance);
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
