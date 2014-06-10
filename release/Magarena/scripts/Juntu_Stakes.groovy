[
    new MagicStatic(MagicLayer.Game) {
        @Override
        public void modGame(final MagicPermanent source, final MagicGame game) {
            final Collection<MagicPermanent> targets =
                    game.filterPermanents(source.getController(),MagicTargetFilterFactory.CREATURE_POWER_1_OR_LESS);
            for (final MagicPermanent creature : targets) {
                if (!creature.hasAbility(MagicAbility.DoesNotUntap)) {
                    final Set<MagicAbility> flags = creature.getAbilityFlags()
                    creature.addAbility(MagicAbility.DoesNotUntap,flags);
                }
            }
        }
    }
]
