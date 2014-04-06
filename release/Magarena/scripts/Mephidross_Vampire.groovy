[
    new MagicStatic(
        MagicLayer.Type,
        MagicTargetFilterFactory.TARGET_CREATURE_YOU_CONTROL
    ) {
         @Override
         public void modSubTypeFlags(final MagicPermanent permanent, final Set<MagicSubType> flags) {
             flags.add(MagicSubType.Vampire);
         }
    }
]
