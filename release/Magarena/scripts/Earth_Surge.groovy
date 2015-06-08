[
    new MagicStatic(MagicLayer.ModPT, LAND) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent target, final MagicPowerToughness pt) {
            if (target.hasType(MagicType.Creature)) {
                pt.add(3,3);
            }
        }
    }
]
