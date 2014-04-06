[
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            if (source.getController().controlsPermanent(MagicTargetFilterFactory.TARGET_BLACK_CREATURE_YOU_CONTROL)) {
                permanent.addAbility(MagicAbility.Persist, flags);
            }
        }
    },
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            if (source.getController().controlsPermanent(MagicTargetFilterFactory.TARGET_RED_CREATURE_YOU_CONTROL)) {
                permanent.addAbility(MagicAbility.Haste, flags);
            }
        }
    }
    
]
