[
    Bant_Sureblade.S1,
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            if (Bant_Sureblade.isValid(permanent)) {
                flags.add(MagicAbility.Haste);
            }
        }
    }
]
