[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game,final MagicPlayer player,final MagicPowerToughness pt) {
            final int amount =
                player.getNrOfPermanentsWithSubType(MagicSubType.Cleric) +
                player.getOpponent().getNrOfPermanentsWithSubType(MagicSubType.Cleric);
            pt.set(amount,amount);
        }
    }
]
