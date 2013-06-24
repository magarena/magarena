[
    new MagicStatic(
        MagicLayer.Ability,
        MagicTargetFilter.TARGET_ARTIFACT_YOU_CONTROL) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            flags.add(MagicAbility.Shroud);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return MagicCondition.METALCRAFT_CONDITION.accept(source);
        }
    }
]
