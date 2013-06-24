[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game,final MagicPlayer player,final MagicPowerToughness pt) {
            final int size = player.getNrOfPermanentsWithType(MagicType.Land);
            pt.set(size, size);
        }
    }
]
