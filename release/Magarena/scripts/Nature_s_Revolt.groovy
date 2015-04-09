[
    new MagicStatic(
        MagicLayer.SetPT,
        LAND
    ) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.set(2,2);
        }
    },
    new MagicStatic(
        MagicLayer.Type,
        LAND
    ) {
        @Override
        public int getTypeFlags(final MagicPermanent permanent,final int flags) {
            return flags|MagicType.Creature.getMask();
        }
    }
]
