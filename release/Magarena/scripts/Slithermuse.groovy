[
    new MagicWhenLeavesPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicRemoveFromPlayAction act) {
            return act.isPermanent(permanent) &&
                   permanent.getOpponent().getHandSize() >
                   permanent.getController().getHandSize() ?
                new MagicEvent(
                    permanent,
                    this,
                    "If opponent has more cards in hand than you, draw cards equal to the difference."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getPermanent().getOpponent().getHandSize() - 
                               event.getPermanent().getController().getHandSize();
            game.doAction(new MagicDrawAction(event.getPlayer(),amount));
        }
    }
]
