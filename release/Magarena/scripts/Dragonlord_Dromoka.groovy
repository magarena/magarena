[
    new MagicStatic(MagicLayer.Player) {
        @Override
        public void modPlayer(final MagicPermanent source, final MagicPlayer player) {
            if (source.isOpponent(player)) {
                player.setState(MagicPlayerState.CantCastSpells);
            }
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source.isController(game.getTurnPlayer())
        }
    }
]
