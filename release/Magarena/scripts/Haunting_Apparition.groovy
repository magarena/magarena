[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game, final MagicPlayer player, final MagicPermanent permanent, final MagicPowerToughness pt) {
            final int amt = GREEN_CREATURE_CARD_FROM_GRAVEYARD.filter(permanent.getChosenPlayer()).size();
            pt.setPower(1 + amt);
        }
    }
]
