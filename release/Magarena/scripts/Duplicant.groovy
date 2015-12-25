[
    new MagicStatic(MagicLayer.SetPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final MagicCard card = source.getExiledCard();
            pt.set(card.getPower(),card.getToughness());
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return MagicCondition.HAS_EXILED_CREATURE_CARD.accept(source);
        }
    }, 
    new MagicStatic(MagicLayer.Type) {
        @Override
        public void modSubTypeFlags(final MagicPermanent source, final Set<MagicSubType> flags) {
            final MagicCard card = source.getExiledCard();
            flags.addAll(card.getSubTypes());
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return MagicCondition.HAS_EXILED_CREATURE_CARD.accept(source);
        }
    }
]
