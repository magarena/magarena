[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game, final MagicPlayer player, final MagicPermanent permanent, final MagicPowerToughness pt) {
            int amount = 0;
            final Collection<MagicPermanent> targets = game.filterPermanents(
                player,
                MagicTargetFilterFactory.CREATURE_YOU_CONTROL
            );
            for (final MagicPermanent creature:targets) {
                if (creature != permanent) {
                    amount += creature.getConvertedCost();
                }
            }
            pt.set(amount, amount);
        }
    }
]
