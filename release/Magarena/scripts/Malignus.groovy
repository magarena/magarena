[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game, final MagicPlayer player, final MagicPermanent permanent, final MagicPowerToughness pt) {
            def amount = player.getOpponent().getHalfLifeRoundUp();
            pt.set(amount,amount);
        }
    }
]
