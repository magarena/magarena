[
    new MagicStatic(
        MagicLayer.ModPT,
        MagicTargetFilter.TARGET_CAT_YOU_CONTROL) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.add(2,2);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source.isEquipped();
        }
    },
    new MagicStatic(
        MagicLayer.Ability,
        MagicTargetFilter.TARGET_CAT_YOU_CONTROL) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            flags.add(MagicAbility.DoubleStrike);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source.isEquipped();
        }
    }
]
