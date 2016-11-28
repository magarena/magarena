[
    new MagicStatic(MagicLayer.Player) {
        @Override
        public void modPlayer(final MagicPermanent source, final MagicPlayer player) {
            if (player == source.getChosenPlayer()) {
                player.setMaxHandSize(4);
            }
        }
    }
]
