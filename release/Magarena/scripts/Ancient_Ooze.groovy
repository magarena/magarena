[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game, final MagicPlayer player, final MagicPermanent permanent, final MagicPowerToughness pt) {
            int amount = 0;
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.CREATURE_YOU_CONTROL.except(permanent);
            game.filterPermanents(player,filter) each {
                amount += it.getConvertedCost();
            }
            pt.set(amount, amount);
        }
    }
]
