[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game,final MagicPlayer player,final MagicPowerToughness pt) {
            final int amount =
                player.getNrOfPermanents(MagicSubType.Elf) +
                player.getOpponent().getNrOfPermanents(MagicSubType.Elf);
            pt.set(amount,amount);
        }
    }
]
