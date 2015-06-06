[
    new MagicStatic(MagicLayer.Player) {
        @Override
        public void modPlayer(final MagicPermanent source, final MagicPlayer player) {
            if (player != player.getGame().getTurnPlayer()) {
                player.setState(MagicPlayerState.CantCastSpells);
            }
        }
    }
]
