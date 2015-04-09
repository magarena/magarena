[
    new MagicStatic(
        MagicLayer.SetPT,
        SWAMP
    ) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.set(1,1);
        }
    },
    new MagicStatic(
        MagicLayer.Color,
        SWAMP
    ) {
        @Override
        public int getColorFlags(final MagicPermanent permanent, final int flags) {
            return flags|MagicColor.Black.getMask();
        }
    },
    new MagicStatic(
        MagicLayer.Type,
        SWAMP
    ) {
        @Override
        public int getTypeFlags(final MagicPermanent permanent,final int flags) {
            return flags|MagicType.Creature.getMask();
        }
    }
]
