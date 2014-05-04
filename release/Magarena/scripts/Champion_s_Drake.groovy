[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final MagicGame game = source.getGame();
        	final int boost = 0;
            final Collection<MagicPermanent> targets = game.filterPermanents(
                    permanent.getController(),
                    MagicTargetFilterFactory.CREATURE_YOU_CONTROL);
            for (final MagicPermanent target : targets) {
                if (target.getCounters(MagicCounterType.Level) >= 3) {
                    boost = 3;
                }
            }
            pt.add(boost,boost);
        }
    }
]
