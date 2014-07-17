[
    new MagicStatic(
        MagicLayer.Ability
    ) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            permanent.addAbility(MagicAbility.CannotAttack, flags);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            final MagicPlayer opponent = source.getController().getOpponent();
            return !opponent.controlsPermanent(MagicType.Enchantment) && 
                !opponent.controlsPermanent(MagicTargetFilterFactory.ENCHANTED_PERMANENT);
        }
    }
]
