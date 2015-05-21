[
    new MagicStatic(MagicLayer.ModPT, CREATURE_YOU_CONTROL) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent target, final MagicPowerToughness pt) {
            int amount = source.getController().getNrOfPermanents(MagicSubType.Gate);
            pt.add(0,amount);
        }
    }
]
