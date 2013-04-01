[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            if (MagicCondition.CONTROL_ANOTHER_MULTICOLORED_PERMANENT.accept(permanent)) {
                pt.add(1,1);
            }
        }        
    },
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            if (MagicCondition.CONTROL_ANOTHER_MULTICOLORED_PERMANENT.accept(permanent)) {
                flags.add(MagicAbility.Flying);
            }
        }
    }
]
