[
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            if (permanent.getController().controlsPermanent(MagicTargetFilter.TARGET_LEGENDARY_SAMURAI_YOU_CONTROL)) {
                permanent.addAbility(MagicAbility.Vigilance, flags);
            }
        }
    },
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            if (permanent.getController().controlsPermanent(MagicTargetFilter.TARGET_LEGENDARY_SAMURAI_YOU_CONTROL)) {
                pt.add(1,2);
            }
        }
    }
]
