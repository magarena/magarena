def TARGET_CREATURE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature();
        }
    };
[
    new MagicStatic(
        MagicLayer.ModPT,
        TARGET_CREATURE) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            int X = 0 - permanent.getController().getHandSize();
            pt.add(X,0);
        }
    }
]
