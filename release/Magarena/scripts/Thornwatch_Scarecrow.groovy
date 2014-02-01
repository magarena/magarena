[
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            if (source.getController().controlsPermanent(MagicTargetFilter.TARGET_GREEN_CREATURE_YOU_CONTROL)) {
                permanent.addAbility(MagicAbility.Wither);
            }
        }
    },
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            if (source.getController().controlsPermanent(MagicTargetFilter.TARGET_WHITE_CREATURE_YOU_CONTROL)) {
                permanent.addAbility(MagicAbility.Vigilance);
            }
        }
    }
    
]
