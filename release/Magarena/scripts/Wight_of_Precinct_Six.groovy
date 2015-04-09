[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final int size = source.getOpponent().filterCards(CREATURE_CARD_FROM_GRAVEYARD).size();
            pt.add(size,size);
        }
    }
]
