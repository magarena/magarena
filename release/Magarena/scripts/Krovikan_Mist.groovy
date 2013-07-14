[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game,final MagicPlayer player,final MagicPowerToughness pt) {
            final int amount =
                player.getNrOfPermanents(MagicSubType.Illusion) +
                player.getOpponent().getNrOfPermanents(MagicSubType.Illusion);
            pt.set(amount,amount);
        }
    }
]
