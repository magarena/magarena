[
    new MagicStatic(MagicLayer.Game) {
        @Override
        public void modGame(final MagicPermanent source, final MagicGame game) {
            CREATURE_POWER_1_OR_LESS.filter(game) each {
                it.addAbility(MagicAbility.DoesNotUntap);
            }
        }
    }
]
