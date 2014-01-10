[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game,final MagicPlayer player,final MagicPowerToughness pt) {
            final int tough = player.getNrOfPermanents(MagicTargetFilter.TARGET_GREEN_CREATURE)+player.getOpponent().getNrOfPermanents(MagicTargetFilter.TARGET_GREEN_CREATURE);
            final int power = player.getNrOfPermanents(MagicTargetFilter.TARGET_RED_CREATURE)+player.getOpponent().getNrOfPermanents(MagicTargetFilter.TARGET_RED_CREATURE);
            pt.set(power, tough);
        }
    }
]
