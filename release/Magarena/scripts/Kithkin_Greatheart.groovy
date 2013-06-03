[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            if (source.getController().controlsPermanent(MagicSubType.Giant)) {
                pt.add(1,1);
            }
        }
    },

    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            if (source.getController().controlsPermanent(MagicSubType.Giant)) {
                flags.add(MagicAbility.FirstStrike);
            }
        }
    }
]
