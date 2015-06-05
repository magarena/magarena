[
    new MagicStatic(MagicLayer.Player) {
        @Override
        public void modPlayer(final MagicPermanent source, final MagicPlayer player) {
            source.getController().reduceMaxHandSize(3);
        }
    }
]
