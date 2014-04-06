[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game,final MagicPlayer player,final MagicPowerToughness pt) {
            final int tough = player.getNrOfPermanents(MagicTargetFilterFactory.TARGET_GREEN_CREATURE) + 
                player.getOpponent().getNrOfPermanents(MagicTargetFilterFactory.TARGET_GREEN_CREATURE);
            pt.setToughness(tough+1);
        }
    }
]
