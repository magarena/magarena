[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return permanent.isOpponent(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Exile all cards from PN's hand."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCardList hand = new MagicCardList(event.getPlayer().getHand());
            for (final MagicCard card : hand) {
                game.doAction(new ExileLinkAction(event.getPermanent(), card, MagicLocationType.OwnersHand));
            }
        }
    },
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "Return all cards PN owns exiled with SN to his or her hand, then draw a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ReturnLinkedExileAction(event.getPermanent(), MagicLocationType.OwnersHand));
            game.doAction(new DrawAction(event.getPlayer(), 1));
        }
    }
]
