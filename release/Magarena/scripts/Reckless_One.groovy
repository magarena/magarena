[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game,final MagicPlayer player,final MagicPowerToughness pt) {
            final int amount = 
                player.getNrOfPermanentsWithSubType(MagicSubType.Goblin) +
                player.getOpponent().getNrOfPermanentsWithSubType(MagicSubType.Goblin); 
            pt.set(amount,amount);
        }
    }
]
