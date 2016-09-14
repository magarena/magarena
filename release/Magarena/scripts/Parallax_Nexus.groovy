def action = {
    final MagicGame game, final MagicEvent event ->
    event.processTargetCard(game, {
        game.doAction(new ExileLinkAction(
            event.getPermanent(),
            it,
            MagicLocationType.OwnersHand
        ));
    });
}

[
    new MagicPermanentActivation(
        [
            MagicCondition.SORCERY_CONDITION,
        ],
        new MagicActivationHints(MagicTiming.Main),
        "Exile"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicRemoveCounterEvent(source,MagicCounterType.Fade,1)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_OPPONENT,
                this,
                "Target opponent\$ exiles a card from his or her hand."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    it,
                    A_CARD_FROM_HAND,
                    action,
                    "PN exiles a card from his or her hand."
                ));
            });
        }
    },
    new ThisLeavesBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final RemoveFromPlayAction act) {
            return new MagicEvent(
                permanent,
                this,
                "Return exiled cards to their owner's hand"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ReturnLinkedExileAction(event.getPermanent(),MagicLocationType.OwnersHand));
        }
    }
]
