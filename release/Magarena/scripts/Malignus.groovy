[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game, final MagicPlayer player, final MagicPermanent permanent, final MagicPowerToughness pt) {
            def amount = (player.getOpponent().getLife() + 1).intdiv(2);
            pt.set(amount,amount);
        }
    }
]
