[
    new MagicStatic(
        MagicLayer.SetPT,
        FOREST
    ) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.set(1,1);
        }
    },
    new MagicStatic(
        MagicLayer.Type,
        FOREST
    ) {
        @Override
        public int getTypeFlags(final MagicPermanent permanent,final int flags) {
            return flags|MagicType.Creature.getMask();
        }
    }
]
