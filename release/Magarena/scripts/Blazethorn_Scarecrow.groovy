[
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            if (source.getController().controlsPermanent(MagicTargetFilter.TARGET_RED_CREATURE_YOU_CONTROL)) {
                permanent.addAbility(MagicAbility.Haste);
            }
        }
    },
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            if (source.getController().controlsPermanent(MagicTargetFilter.TARGET_GREEN_CREATURE_YOU_CONTROL)) {
                permanent.addAbility(MagicAbility.Wither);
            }
        }
    }
    
]
