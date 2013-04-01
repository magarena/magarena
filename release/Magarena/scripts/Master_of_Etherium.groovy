[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game, final MagicPlayer player, final MagicPowerToughness pt) {
            final int size = game.filterPermanents(player, MagicTargetFilter.TARGET_ARTIFACT_YOU_CONTROL).size();
            pt.set(size, size);
        }
    },
    new MagicStatic(
        MagicLayer.ModPT, 
        MagicTargetFilter.TARGET_ARTIFACT_CREATURE_YOU_CONTROL) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.add(1,1);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source != target;
        }
    }
]
