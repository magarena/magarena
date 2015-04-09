[
    new MagicStatic(
        MagicLayer.Type,
        CREATURE_YOU_CONTROL
    ) {
         @Override
         public void modSubTypeFlags(final MagicPermanent permanent, final Set<MagicSubType> flags) {
             flags.add(MagicSubType.Sliver);
         }
    }
]
