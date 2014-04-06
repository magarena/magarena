[
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            if (source.getController().controlsPermanent(MagicTargetFilterFactory.TARGET_BLUE_CREATURE_YOU_CONTROL)) {
                permanent.addAbility(MagicAbility.Flying, flags);
            }
        }
    },
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            if (source.getController().controlsPermanent(MagicTargetFilterFactory.TARGET_BLACK_CREATURE_YOU_CONTROL)) {
                permanent.addAbility(MagicAbility.Persist, flags);
            }
        }
    }
    
]
