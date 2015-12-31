[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
            for (final MagicColor color : MagicColor.values()) {
                if (permanent.hasColor(color)) {
                    pt.add(1, 1);
                }
            }
        }
        @Override
        public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return MagicStatic.acceptLinked(game, source, target);
        }
    }
]
