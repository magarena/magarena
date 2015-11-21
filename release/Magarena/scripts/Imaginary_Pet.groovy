[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return upkeepPlayer.getHandSize() >= 1 ?
                new MagicEvent(
                    permanent,
                    this,
                    "If PN has a card in hand, return SN to its owner's hand."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getPlayer().getHandSize() >= 1) {
                game.doAction(new RemoveFromPlayAction(event.getPermanent(),MagicLocationType.OwnersHand));
            }
        }
    }
]
