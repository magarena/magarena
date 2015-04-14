[
    new MagicWhenSelfLeavesPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicRemoveFromPlayAction act) {
            return permanent.getOpponent().getHandSize() >
                   permanent.getController().getHandSize() ?
                new MagicEvent(
                    permanent,
                    this,
                    "If opponent has more cards in hand than PN, PN draws cards equal to the difference."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getPlayer().getOpponent().getHandSize() - 
                               event.getPlayer().getHandSize();
            game.doAction(new DrawAction(event.getPlayer(),amount));
        }
    }
]
