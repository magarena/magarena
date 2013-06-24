[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            if (permanent.isEnchanted()) {
                pt.add(2,2);
            }
        }
    },
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            if (permanent.isEnchanted()) {
                flags.add(MagicAbility.Flying);
                flags.add(MagicAbility.FirstStrike);
                flags.add(MagicAbility.Trample);
            }
        }
    }
]
