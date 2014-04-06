[
    new MagicStatic(
        MagicLayer.Type,
        MagicTargetFilterFactory.CREATURE_YOUR_OPPONENT_CONTROLS
    ) {
         @Override
         public void modSubTypeFlags(final MagicPermanent permanent, final Set<MagicSubType> flags) {
             flags.add(MagicSubType.Illusion);
         }
    }
]
