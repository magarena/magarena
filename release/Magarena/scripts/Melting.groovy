[
    new MagicStatic(MagicLayer.Type,LAND) {
        @Override
        public int getTypeFlags(final MagicPermanent permanent,final int flags) {
            return flags & ~MagicType.Snow.getMask();
        }
    }
]
