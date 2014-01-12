[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game,final MagicPlayer player,final MagicPowerToughness pt) {
            final int tough = player.getNrOfPermanents(MagicTargetFilter.TARGET_GREEN_CREATURE)+player.getOpponent().getNrOfPermanents(MagicTargetFilter.TARGET_GREEN_CREATURE);
            pt.setToughness(tough+1);
        }
    }
]
