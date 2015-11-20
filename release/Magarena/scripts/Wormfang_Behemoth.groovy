[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "PN exiles all cards from his or her hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCardList hand = new MagicCardList(event.getPlayer().getHand());
            for (final MagicCard card : hand) {
                game.doAction(new ExileLinkAction(
                    event.getPermanent(),
                    card,
                    MagicLocationType.OwnersHand
                ));
            }
        }
    },
    new MagicWhenSelfLeavesPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final RemoveFromPlayAction act) {
            return new MagicEvent(
                permanent,
                this,
                "Return the exiled cards to their owner's hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ReturnLinkedExileAction(
                event.getPermanent(),
                MagicLocationType.OwnersHand
            ));
        }
    }
]
