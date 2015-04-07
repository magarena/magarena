[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return permanent.isOpponent(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Exile all cards from your hand."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCardList hand = new MagicCardList(event.getPlayer().getHand());
            for (final MagicCard card : hand) {
                game.doAction(new MagicExileLinkAction(event.getPermanent(), card, MagicLocationType.OwnersHand));
            }
        }
    },
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "Return all cards you own exiled with SN to PN's hand, then draw a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicReturnLinkedExileAction(event.getPermanent(), MagicLocationType.OwnersHand));
            game.doAction(new MagicDrawAction(event.getPlayer(), 1));
        }
    }
]
