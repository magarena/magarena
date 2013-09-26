[    
    new MagicStatic(
        MagicLayer.Ability,
        MagicTargetFilter.TARGET_KALDRA_EQUIPMENT
    ) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            flags.add(MagicAbility.Indestructible);
        }
    }
]
