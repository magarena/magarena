[
    new MagicStatic(MagicLayer.Game) {
        @Override
        public void modGame(final MagicPermanent source, final MagicGame game) {
            final Collection<MagicPermanent> targets = game.filterPermanents(CREATURE_POWER_3_OR_MORE);
            for (final MagicPermanent creature : targets) {
                creature.addAbility(MagicAbility.DoesNotUntap);
            }
        }
    }
]
