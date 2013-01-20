[
    new MagicStatic(MagicLayer.Player) {
        @Override
        public void modPlayer(final MagicPermanent source,final MagicPermanent permanent) {
            source.getController().addAbility(MagicAbility.Hexproof);
        }
    }
]
