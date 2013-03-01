[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game,final MagicPlayer player,final MagicPowerToughness pt) {
            final amt = player.getLife();
            pt.set(amt, amt);
        }
    }
]
