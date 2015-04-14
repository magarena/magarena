[
    new MagicStatic(MagicLayer.Type, NONCREATURE_ARTIFACT) {
        @Override
        public int getTypeFlags(final MagicPermanent permanent, final int flags) {
            return flags|MagicType.Artifact.getMask()|MagicType.Creature.getMask();
        }
    },
    new MagicStatic(MagicLayer.SetPT, ARTIFACT_CREATURE) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            if (MagicCondition.WAS_NONCREATURE_ARTIFACT.accept(permanent)) {
                final int cmc = permanent.getConvertedCost();
                pt.set(cmc,cmc);
            }
        }
    }
]
