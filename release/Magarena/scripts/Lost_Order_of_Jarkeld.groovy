[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game, final MagicPlayer player, final MagicPermanent permanent, final MagicPowerToughness pt) {
            final int amt = permanent.getChosenPlayer().getNrOfPermanents(MagicType.Creature);
            pt.set(1 + amt, 1 + amt);
        }
    }
]
