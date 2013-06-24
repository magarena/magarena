[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game,final MagicPlayer player,final MagicPowerToughness pt) {
            final int amount =
                player.getNrOfPermanentsWithSubType(MagicSubType.Elf) +
                player.getOpponent().getNrOfPermanentsWithSubType(MagicSubType.Elf);
            pt.set(amount,amount);
        }
    }
]
