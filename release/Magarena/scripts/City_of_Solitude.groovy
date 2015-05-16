[
    new MagicStatic(MagicLayer.Player) {
        @Override
        public void modPlayer(final MagicPermanent source, final MagicPlayer player) {
            source.getController().setState(MagicPlayerState.CantCastSpells);
            source.getController().setState(MagicPlayerState.CantActivateAbilities);

        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return game.getTurnPlayer() == source.getOpponent();
        }
    },
    new MagicStatic(MagicLayer.Player) {
        @Override
        public void modPlayer(final MagicPermanent source, final MagicPlayer player) {
            source.getOpponent().setState(MagicPlayerState.CantCastSpells);
            source.getOpponent().setState(MagicPlayerState.CantActivateAbilities);

        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return game.getTurnPlayer() == source.getController();
        }
    }
]
