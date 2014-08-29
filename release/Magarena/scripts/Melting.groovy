[
    new MagicStatic(MagicLayer.Type,MagicTargetFilterFactory.LAND) {
        @Override
        public int getTypeFlags(final MagicPermanent permanent,final int flags) {
            return flags & ~MagicType.Snow.getMask();
        }
]
