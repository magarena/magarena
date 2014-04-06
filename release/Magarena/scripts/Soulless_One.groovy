[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game,final MagicPlayer player,final MagicPowerToughness pt) {
            final int battlefield =
                player.getNrOfPermanents(MagicSubType.Zombie) +
                player.getOpponent().getNrOfPermanents(MagicSubType.Zombie);
            final int graveyard =
                game.filterCards(player,MagicTargetFilterFactory.ZOMBIE_CARD_FROM_GRAVEYARD).size() +
                game.filterCards(player.getOpponent(),MagicTargetFilterFactory.ZOMBIE_CARD_FROM_GRAVEYARD).size();
            final int amount = battlefield + graveyard;
            pt.set(amount,amount);
        }
    }
]
