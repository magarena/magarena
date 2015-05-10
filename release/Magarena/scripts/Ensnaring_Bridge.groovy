[
    new MagicStatic(MagicLayer.Game) {
        @Override
        public void modGame(final MagicPermanent source, final MagicGame game) {
            new MagicPTTargetFilter(
                CREATURE,
                Operator.GREATER_THAN,
                source.getController().getHandSize()
            ).filter(game) each {
                it.addAbility(MagicAbility.CannotAttack);
            }
        }
    }
]
