[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game,final MagicPlayer player,final MagicPowerToughness pt) {
            final int amount =
                player.getNrOfPermanentsWithSubType(MagicSubType.Illusion) +
                player.getOpponent().getNrOfPermanentsWithSubType(MagicSubType.Illusion);
            pt.set(amount,amount);
        }
    }
]
