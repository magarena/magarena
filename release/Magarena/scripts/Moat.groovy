[
    new MagicStatic(MagicLayer.Game) {
        @Override
        public void modGame(final MagicPermanent source, final MagicGame game) {
            final Collection<MagicPermanent> targets = game.filterPermanents(MagicTargetFilterFactory.CREATURE_WITHOUT_FLYING);
            for (final MagicPermanent creature : targets) {
                creature.addAbility(MagicAbility.CannotAttack);
            }
        }
    }
]
