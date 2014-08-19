[
    new MagicStatic(MagicLayer.Type,MagicTargetFilterFactory.LAND) {
         @Override
         public void modSubTypeFlags(final MagicPermanent permanent, final Set<MagicSubType> flags) {
             flags.add(MagicSubType.Island);
         }
    }
]
