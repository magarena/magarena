[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            if (permanent.getController().controlsPermanent(MagicSubType.Griffin)) {
                pt.add(3,3);
            }
        }
    },
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            if (permanent.getController().controlsPermanent(MagicSubType.Griffin)) {
                flags.add(MagicAbility.Flying);
            }
        }
    }
]
