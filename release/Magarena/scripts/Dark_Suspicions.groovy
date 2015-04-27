[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return permanent.isOpponent(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    permanent.getController(),
                    upkeepPlayer,
                    this,
                    "RN loses X life, where X is the number of cards in RN's hand minus the number of cards in PN's hand."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getRefPlayer().getHandSize() - event.getPlayer().getHandSize();
            game.logAppendMessage(event.getPlayer()," (X="+amount+")");
            if (amount > 0) {
                game.doAction(new ChangeLifeAction(event.getRefPlayer(),-amount));
            }
        }
    }
]
