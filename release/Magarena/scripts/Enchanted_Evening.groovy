[
    new MagicStatic(MagicLayer.Type,MagicTargetFilterFactory.PERMANENT) {
        @Override
        public int getTypeFlags(final MagicPermanent permanent, final int flags) {
            return flags | MagicType.Enchantment.getMask();
        }
    }
]
