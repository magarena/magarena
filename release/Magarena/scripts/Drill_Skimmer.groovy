[
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final MagicPermanentFilterImpl filter = new MagicOtherPermanentTargetFilter(MagicTargetFilter.TARGET_ARTIFACT_CREATURE, source);
            if (source.getController().controlsPermanent(filter)) {
                flags.add(MagicAbility.Flying);
            }
        }
    }
]
