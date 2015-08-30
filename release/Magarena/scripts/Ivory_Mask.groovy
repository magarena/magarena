[
    new MagicStatic(MagicLayer.Player) {
        @Override
        public void modPlayer(final MagicPermanent source, final MagicPlayer player) {
            if (source.isController(player)) {
                player.addAbility(MagicAbility.Shroud);
            }
        }
    }
]
