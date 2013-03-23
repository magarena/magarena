[
    new MagicWhenDiscardedTrigger() {
        @Override
        public MagicEvent getEvent(final MagicPermanent permanent,final MagicCard card) {
            final MagicPlayer otherController = card.getOwner();
            final MagicPlayer player = permanent.getController();
            return (otherController != player) ?
                new MagicEvent(
                        permanent,
                        otherController,
                        this,
                        "PN loses 2 life."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            game.doAction(new MagicChangeLifeAction(event.getPlayer(),-2));
        }
    }
]
