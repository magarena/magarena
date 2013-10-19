[
    new MagicWhenDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicMoveCardAction data) {
            return new MagicEvent(
                permanent,
                permanent.getOpponent(),
                this,
                "PN loses 2 life."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeLifeAction(event.getPlayer(),-2));
        }
    }
]
