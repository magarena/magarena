[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game,final MagicPlayer player,final MagicPowerToughness pt) {
            final int amount = 1 + player.getOpponent().getNrOfPermanents(MagicType.Creature);
            pt.set(amount,amount);
        }
    }
]
