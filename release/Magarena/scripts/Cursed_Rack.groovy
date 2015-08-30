[
    new MagicStatic(MagicLayer.Player) {
        @Override
        public void modPlayer(final MagicPermanent source, final MagicPlayer player) {
            if (source.isOpponent(player)) {
                player.setMaxHandSize(4);
            }
        }
    }
]
