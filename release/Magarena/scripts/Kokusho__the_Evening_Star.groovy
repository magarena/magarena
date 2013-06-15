[
    new MagicWhenDiesTrigger() {
        @Override
        public MagicEvent getEvent(final MagicPermanent permanent) {
            return new MagicEvent(
                permanent,
                this,
                "Your opponent loses 5 life. You gain 5 life."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player=event.getPlayer();
            game.doAction(new MagicChangeLifeAction(player.getOpponent(),-5));
            game.doAction(new MagicChangeLifeAction(player,5));
        }
    }
]
