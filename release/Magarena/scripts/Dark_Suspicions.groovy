[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
           return permanent.isOpponent(upkeepPlayer) ?
               new MagicEvent(
                    permanent,
                    this,
                    "Opponent loses X life, where X is the number of cards in his or her hand minus the number of cards in your hand."
                ):
                    MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getPermanent().getOpponent().getHandSize() - 
                               event.getPermanent().getController().getHandSize();
            if (amount > 0) {
            game.doAction(new MagicChangeLifeAction(event.getPermanent().getOpponent(),-amount));
            }
        }
    }
]
