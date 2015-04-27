[
    new MagicStatic(MagicLayer.Type, LAND) {
        @Override
        public void modSubTypeFlags(final MagicPermanent permanent, final Set<MagicSubType> flags) {
            flags.add(MagicSubType.Swamp);
        }
    }
]
