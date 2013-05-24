[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game,final MagicPlayer player,final MagicPowerToughness pt) {
            final int amount = 
                player.getNrOfPermanentsWithType(MagicType.Creature) +
                player.getOpponent().getNrOfPermanentsWithType(MagicType.Creature); 
            pt.set(amount,amount);
        }
    }
]
