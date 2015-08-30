[
    new MagicStatic(MagicLayer.Type,GOBLIN_CREATURE) {
        @Override
        public void modSubTypeFlags(final MagicPermanent permanent, final Set<MagicSubType> flags) {
            flags.add(MagicSubType.Zombie);
        }
    },
    
    new MagicStatic(MagicLayer.Color,GOBLIN_CREATURE) {
        @Override
        public int getColorFlags(final MagicPermanent permanent, final int flags) {
            return flags | MagicColor.Black.getMask();
        }
    }
]
