[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game, final MagicPlayer player, final MagicPermanent permanent, final MagicPowerToughness pt) {
            int amount = 0;
            final MagicTargetFilter<MagicPermanent> filter = new MagicOtherPermanentTargetFilter(
                MagicTargetFilterFactory.CREATURE_YOU_CONTROL,
                permanent
            );
            final Collection<MagicPermanent> targets = game.filterPermanents(player,filter);
            for (final MagicPermanent creature:targets) {
                amount += creature.getConvertedCost();
            }
            pt.set(amount, amount);
        }
    }
]
