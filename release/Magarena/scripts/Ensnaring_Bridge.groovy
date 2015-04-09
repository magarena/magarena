[
    new MagicStatic(MagicLayer.Game) {
        @Override
        public void modGame(final MagicPermanent source, final MagicGame game) {
            final MagicTargetFilter<MagicPermanent> filter = new MagicPTTargetFilter(
                CREATURE,
                Operator.GREATER_THAN,
                source.getController().getHandSize()
            );
            game.filterPermanents(filter).each {
                it.addAbility(MagicAbility.CannotAttack);
            }
        }
    }
]
