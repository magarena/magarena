[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
            def p = source.getController().getNrOfPermanents(MagicSubType.Forest).intdiv(2);
            def t = (source.getController().getNrOfPermanents(MagicSubType.Forest) + 1).intdiv(2);
            pt.add(p,t);
        }
        @Override
        public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return MagicStatic.acceptLinked(game, source, target);
        }
    }
]
