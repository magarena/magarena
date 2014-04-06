[
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            if (source.getController().controlsPermanent(MagicTargetFilterFactory.RED_CREATURE_YOU_CONTROL)) {
                permanent.addAbility(MagicAbility.Haste, flags);
            }
        }
    },
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            if (source.getController().controlsPermanent(MagicTargetFilterFactory.GREEN_CREATURE_YOU_CONTROL)) {
                permanent.addAbility(MagicAbility.Wither, flags);
            }
        }
    }
    
]
