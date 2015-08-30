[
    new MagicStatic(MagicLayer.Color,CREATURE) {
        @Override
        public int getColorFlags(final MagicPermanent permanent, final int flags) {
            return MagicColor.Black.getMask();
        }
    }
]
