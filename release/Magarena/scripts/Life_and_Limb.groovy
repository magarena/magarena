[
    new MagicStatic(MagicLayer.Type,MagicTargetFilterFactory.FOREST_OR_SAPROLING) {
        @Override
        public int getTypeFlags(final MagicPermanent permanent,final int flags) {
            return flags|MagicType.Creature.getMask()|MagicType.Land.getMask();
        }
    },
    
    new MagicStatic(MagicLayer.SetPT,MagicTargetFilterFactory.FOREST_OR_SAPROLING) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.set(1,1);
        }
    },
    
    new MagicStatic(MagicLayer.Color,MagicTargetFilterFactory.FOREST_OR_SAPROLING) {
        @Override
        public int getColorFlags(final MagicPermanent permanent,final int flags) {
            return flags|MagicColor.Green.getMask();
        }
    },
    
    new MagicStatic(MagicLayer.Type,MagicTargetFilterFactory.FOREST_OR_SAPROLING) {
        @Override
        public void modSubTypeFlags(final MagicPermanent permanent, final Set<MagicSubType> flags) {
            flags.add(MagicSubType.Forest);
            flags.add(MagicSubType.Saproling);
        }
    },
]
