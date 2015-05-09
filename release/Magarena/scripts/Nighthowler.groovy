[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
            final int size = CREATURE_CARD_FROM_ALL_GRAVEYARDS.filter(source.getGame()).size();                             
            pt.add(size,size);
        }
        @Override
        public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source == target || MagicStatic.acceptLinked(game, source, target);
        }
    }
]
