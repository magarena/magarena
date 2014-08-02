[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game, final MagicPlayer player, final MagicPermanent permanent, final MagicPowerToughness pt) {
            final int amt = permanent.getChosenPlayer().getNrOfPermanents(MagicTargetFilterFactory.TAPPED_LAND);
            pt.setPower(amt);
        }
    }
]
