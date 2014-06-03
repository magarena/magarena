[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game, final MagicPlayer player, final MagicPermanent permanent, final MagicPowerToughness pt) {
            final int amount = game.filterPermanents(player,MagicTargetFilterFactory.SOLDIER_OR_WARRIOR_YOU_CONTROL).size();
            pt.set(2+amount, 2+amount);
        }
    }
]
