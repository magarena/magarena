[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
            final int size = source.getController().getNrOfPermanents(MagicType.Creature) +
                             source.getController().getNrOfPermanents(MagicSubType.Aura);
            pt.add(size,size);
        }
        @Override
        public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source == target || MagicStatic.acceptLinked(game, source, target);
        }
    }
]
