[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game,final MagicPlayer player,final MagicPowerToughness pt) {
            final int amount = player.getNrOfPermanentsWithType(MagicType.Artifact);
            pt.set(amount,amount);
        }
    }
]
