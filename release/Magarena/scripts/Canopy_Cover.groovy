[
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(
            final MagicPermanent source,
            final MagicPermanent target,
            final Set<MagicAbility> flags) {
            if (source.getController().getIndex() == 0) {
                flags.add(MagicAbility.CannotBeTheTarget1);
            } else {
                flags.add(MagicAbility.CannotBeTheTarget0);
            }
        }
        @Override
        public boolean accept(
                final MagicGame game,
                final MagicPermanent source,
                final MagicPermanent target) {
            return MagicStatic.acceptLinked(game, source, target);
        }
    }
]
