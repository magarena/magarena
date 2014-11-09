[
    new MagicStatic(
        MagicLayer.Ability,
        MagicTargetFilterFactory.CREATURE
    ) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            permanent.addAbility(MagicAbility.CannotAttack, flags);
        }
        @Override
        public boolean condition(final MagicGame game, final MagicPermanent source, final MagicPermanent target) {
            return target.getPower() > source.getController().getHandSize();
        }
    }
]
