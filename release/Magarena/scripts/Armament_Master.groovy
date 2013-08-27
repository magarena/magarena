[
    new MagicStatic(
        MagicLayer.ModPT,
        MagicTargetFilter.TARGET_KOR_YOU_CONTROL
    ) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final int amount = 2 * source.getEquipmentPermanents().size();
            pt.add(amount, amount);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source != target;
        }
    }
]
