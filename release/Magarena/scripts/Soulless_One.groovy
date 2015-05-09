[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game,final MagicPlayer player,final MagicPowerToughness pt) {
            final int battlefield = player.getNrOfPermanents(MagicSubType.Zombie) + player.getOpponent().getNrOfPermanents(MagicSubType.Zombie);
            final int graveyard = ZOMBIE_CARD_FROM_ALL_GRAVEYARDS.filter(player).size(); 
            final int amount = battlefield + graveyard;
            pt.set(amount,amount);
        }
    }
]
