[
    new MagicStatic(MagicLayer.SetPT, NONCREATURE_ARTIFACT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final int cmc = permanent.getConvertedCost();
            pt.set(cmc,cmc);
        }
    },
    new MagicStatic(MagicLayer.Type, NONCREATURE_ARTIFACT) {
        @Override
        public int getTypeFlags(final MagicPermanent permanent, final int flags) {
            return flags|MagicType.Artifact.getMask()|MagicType.Creature.getMask();
        }
    }
]
