[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game,final MagicPlayer player,final MagicPowerToughness pt) {
            final int amount = player.getDevotion(MagicColor.Green);
            pt.set(amount,amount);
        }
    }
]
