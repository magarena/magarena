[
    new MagicStatic(
        MagicLayer.Ability
    ) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            permanent.addAbility(MagicAbility.CannotAttack, flags);
            permanent.addAbility(MagicAbility.CannotBlock, flags);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            final MagicPlayer player = source.getController()
            return player.getNrOfPermanents(MagicType.Creature) < player.getOpponent().getNrOfPermanents(MagicType.Creature);
        }
    }
]
