[
    new MagicStatic(MagicLayer.Game) {
        @Override
        public void modGame(final MagicPermanent source, final MagicGame game) {
            final Collection<MagicPermanent> targets = game.filterPermanents(CREATURE_POWER_1_OR_LESS);
            for (final MagicPermanent creature : targets) {
                creature.addAbility(MagicAbility.DoesNotUntap);
            }
        }
    }
]
