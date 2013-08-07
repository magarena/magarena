[
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent target, final Set<MagicAbility> flags) {
            flags.add(MagicAbility.CannotBeTheTarget(source.getOpponent()));
        }
        @Override
        public boolean accept(final MagicGame game, final MagicPermanent source, final MagicPermanent target) {
            return MagicStatic.acceptLinked(game, source, target);
        }
    }
]
