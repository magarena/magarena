[
    new AtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer eotPlayer) {
            new MagicEvent(
                permanent,
                permanent.getController(),
                this,
                "Each opponent loses life equal to the life he or she lost this turn."
            )
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer opponent = event.getPlayer().getOpponent();
            game.doAction(new ChangeLifeAction(opponent,-opponent.getLifeLossThisTurn()));
        }
    }
]
