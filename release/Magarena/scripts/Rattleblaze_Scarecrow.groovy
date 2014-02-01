[
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            if (source.getController().controlsPermanent(MagicTargetFilter.TARGET_BLACK_CREATURE_YOU_CONTROL)) {
                permanent.addAbility(MagicPersistTrigger.create());
                flags.add(MagicAbility.Persist);
            }
        }
    },
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            if (source.getController().controlsPermanent(MagicTargetFilter.TARGET_RED_CREATURE_YOU_CONTROL)) {
                flags.add(MagicAbility.Haste);
            }
        }
    }
    
]
