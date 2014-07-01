[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game, final MagicPlayer player, final MagicPowerToughness pt) {
            final int amt = player.getDomain();
            pt.set(2, amt);
        }
    }
]
