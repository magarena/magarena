[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
            final int amount = source.getGame().getNrOfPermanents(MagicType.Creature) - 1;
            pt.add(-amount,-amount);
        }
    }
]
