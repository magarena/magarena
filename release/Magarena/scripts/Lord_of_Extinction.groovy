[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game,final MagicPlayer player,final MagicPowerToughness pt) {
            final int amount=game.getPlayer(0).getGraveyard().size()+game.getPlayer(1).getGraveyard().size();
            pt.set(amount,amount);
        }
    }
]
