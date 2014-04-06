[
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            final MagicPermanentFilterImpl filter = new MagicOtherPermanentTargetFilter(MagicTargetFilterFactory.TARGET_ARTIFACT_CREATURE, source);
            if (source.getController().controlsPermanent(filter)) {
                permanent.addAbility(MagicAbility.Shroud, flags);
            }
        }
    }
]
