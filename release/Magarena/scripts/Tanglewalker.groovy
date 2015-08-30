[
    new MagicStatic(MagicLayer.Ability,CREATURE_YOU_CONTROL) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            if (source.getOpponent().controlsPermanent(ARTIFACT_LAND)) {
                permanent.addAbility(MagicAbility.Unblockable, flags);
            };
        }
    }
]
