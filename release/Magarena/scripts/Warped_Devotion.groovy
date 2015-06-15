[
    new MagicWhenLeavesPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final RemoveFromPlayAction act) {
            return act.getToLocation() == MagicLocationType.OwnersHand ?
                new MagicEvent(
                    permanent,
                    act.getPermanent().getOwner(),
                    this,
                    "PN discards a card."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicDiscardEvent(event.getSource(), event.getPlayer(), 1));
        }
    }
]
