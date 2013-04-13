[
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            if (MagicCondition.METALCRAFT_CONDITION.accept(permanent)) {
                flags.add(MagicAbility.Flying);
            }
        }
    }
]
