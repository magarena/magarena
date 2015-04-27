[
    new MagicStatic(MagicLayer.Ability, CREATURE) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            permanent.addAbility(MagicAbility.Intimidate, flags);
        }
        @Override
        public boolean condition(final MagicGame game, final MagicPermanent source, final MagicPermanent target) {
            return target == source.getEnchantedPermanent() &&
                   source.getController().getNrOfPermanents(MagicType.Creature) == 1;
        }
    }
]
