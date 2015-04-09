[
    new MagicStatic(MagicLayer.SetPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final int amt = source.getGame().filterCards(source.getController(), ARTIFACT_CARD_FROM_GRAVEYARD).size();
            pt.add(amt, 0);
        }
    }
]
