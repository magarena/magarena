[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game,final MagicPlayer player,final MagicPowerToughness pt) {
            final int tough = player.getNrOfPermanents(MagicTargetFilterFactory.GREEN_CREATURE) + 
                player.getOpponent().getNrOfPermanents(MagicTargetFilterFactory.GREEN_CREATURE);
            final int power = player.getNrOfPermanents(MagicTargetFilterFactory.RED_CREATURE) + 
                player.getOpponent().getNrOfPermanents(MagicTargetFilterFactory.RED_CREATURE);
            pt.set(power, tough);
        }
    }
]
