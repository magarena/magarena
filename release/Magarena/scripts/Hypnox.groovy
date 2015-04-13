[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return permanent.hasState(MagicPermanentState.CastFromHand) ? 
                new MagicEvent(
                    permanent,
                    MagicTargetChoice.TARGET_OPPONENT,
                    this,
                    "Exile all cards from target opponent\$'s hand."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicCardList hand = new MagicCardList(it.getHand());
                for (final MagicCard card : hand) {
                    game.doAction(new MagicExileLinkAction(
                        event.getPermanent(),
                        card,
                        MagicLocationType.OwnersHand
                    ));
                }
            });
        }
    },
    new MagicWhenSelfLeavesPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicRemoveFromPlayAction act) {
            return new MagicEvent(
                permanent,
                this,
                "Return the exiled cards to their owner's hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicReturnLinkedExileAction(
                event.getPermanent(),
                MagicLocationType.OwnersHand
            ));
        }
    }
]
