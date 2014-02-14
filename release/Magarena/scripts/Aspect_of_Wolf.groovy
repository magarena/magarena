[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
            final int p = (int)Math.floor(source.getController().getNrOfPermanents(MagicSubType.Forest)/2);
            final int t = (int)Math.ceil(source.getController().getNrOfPermanents(MagicSubType.Forest)/2);
            pt.add(p,t);
        }
        @Override
        public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return MagicStatic.acceptLinked(game, source, target);
        }
    }
]
