[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game,final MagicPlayer player,final MagicPowerToughness pt) {
            final int tough = player.getNrOfPermanents(MagicTargetFilterFactory.TARGET_GREEN_CREATURE) + 
                player.getOpponent().getNrOfPermanents(MagicTargetFilterFactory.TARGET_GREEN_CREATURE);
            final int power = player.getNrOfPermanents(MagicTargetFilterFactory.TARGET_RED_CREATURE) + 
                player.getOpponent().getNrOfPermanents(MagicTargetFilterFactory.TARGET_RED_CREATURE);
            pt.set(power, tough);
        }
    }
]
