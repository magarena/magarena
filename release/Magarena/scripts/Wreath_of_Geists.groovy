[
    new MagicStatic(
        MagicLayer.ModPT,
        MagicTargetFilterFactory.TARGET_CREATURE) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final MagicGame game = source.getGame();
            final int amount = game.filterCards(permanent.getController(),MagicTargetFilterFactory.TARGET_CREATURE_CARD_FROM_GRAVEYARD).size();
            pt.add(amount,amount);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return target == source.getEnchantedPermanent();
        }
    }
]
