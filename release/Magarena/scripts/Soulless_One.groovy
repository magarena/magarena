[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game,final MagicPlayer player,final MagicPowerToughness pt) {
            final int battlefield =
                player.getNrOfPermanentsWithSubType(MagicSubType.Zombie) +
                player.getOpponent().getNrOfPermanentsWithSubType(MagicSubType.Zombie);
            final int graveyard =
                game.filterCards(player,MagicTargetFilter.TARGET_ZOMBIE_CARD_FROM_GRAVEYARD).size() +
                game.filterCards(player.getOpponent(),MagicTargetFilter.TARGET_ZOMBIE_CARD_FROM_GRAVEYARD).size();
            final int amount = battlefield + graveyard;
            pt.set(amount,amount);
        }
    }
]
