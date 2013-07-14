[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game,final MagicPlayer player,final MagicPowerToughness pt) {
            final int amount =
                player.getNrOfPermanents(MagicSubType.Goblin) +
                player.getOpponent().getNrOfPermanents(MagicSubType.Goblin);
            pt.set(amount,amount);
        }
    }
]
