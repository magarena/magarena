[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
            pt.add(1, 1);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source.getController().getPermanents().minus(source).any({
                it.hasSubType(MagicSubType.Merfolk) || it.hasSubType(MagicSubType.Island)
            });
        }
    }
]

