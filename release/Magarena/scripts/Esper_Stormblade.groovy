[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final MagicPermanentFilterImpl filter = new MagicOtherPermanentTargetFilter(MagicTargetFilter.TARGET_MULTICOLORED_PERMANENT, source);
            if (source.getController().controlsPermanent(filter)) {
                pt.add(1,1);
            }
        }
    },
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            final MagicPermanentFilterImpl filter = new MagicOtherPermanentTargetFilter(MagicTargetFilter.TARGET_MULTICOLORED_PERMANENT, source);
            if (source.getController().controlsPermanent(filter)) {
                flags.add(MagicAbility.Flying);
            }
        }
    }
]
