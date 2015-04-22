[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final MagicCard card = source.getExiledCard();
            pt.add(card.getPower(),card.getToughness());
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return MagicCondition.HAS_EXILED_CREATURE_CARD.accept(source);
        }
    }
]
