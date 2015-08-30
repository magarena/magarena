[
    new MagicStatic(MagicLayer.Game) {
        @Override
        public void modGame(final MagicPermanent source, final MagicGame game) {
            CREATURE_POWER_3_OR_MORE.filter(source) each {
                it.addAbility(MagicAbility.DoesNotUntap);
            }
        }
    }
]
