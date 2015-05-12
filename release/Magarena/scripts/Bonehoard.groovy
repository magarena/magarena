[
    new MagicStatic(MagicLayer.ModPT, CREATURE) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final int amount = CREATURE_CARD_FROM_ALL_GRAVEYARDS.filter(source.getController()).size();
            pt.add(amount,amount);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return (source.isEquipment()) ?
                source.getEquippedCreature() == target :
                source.getEnchantedPermanent() == target;
        }
    }
]
