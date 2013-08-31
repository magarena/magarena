def action = {
    final MagicGame game, final MagicEvent event ->
    event.processTargetCard(game,new MagicCardAction() {
        public void doAction(final MagicCard card) {
            game.doAction(new MagicExileUntilThisLeavesPlayAction(
                event.getPermanent(),
                card,
                MagicLocationType.OwnersHand
            ));
        }
    });
} as MagicEventAction

[
    new MagicPermanentActivation(
        [
            MagicCondition.SORCERY_CONDITION,
        ],
        new MagicActivationHints(MagicTiming.Main),
        "Exile"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicRemoveCounterEvent(source,MagicCounterType.Charge,1)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.TARGET_OPPONENT,
                this,
                "Target opponent\$ exiles a card from his or her hand."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicPlayer player ->
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    player,
                    MagicTargetChoice.TARGET_CARD_FROM_HAND,
                    action,
                    "PN exiles a card from his or her hand."
                ));
            } as MagicPlayerAction);
        }
    },
    new MagicWhenLeavesPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicRemoveFromPlayAction act) {
            if (act.isPermanent(permanent) && !permanent.getExiledCards().isEmpty()) {
                final MagicCardList clist = new MagicCardList(permanent.getExiledCards());
                return new MagicEvent(
                    permanent,
                    this,
                    clist.size() == 1 ?
                        "Return " + clist.get(0) + " to its owner's hand" :
                        "Return exiled cards to their owner's hand"
                );
            }
            return MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent();
            game.doAction(new MagicReturnExiledUntilThisLeavesPlayAction(permanent,MagicLocationType.OwnersHand));
        }
    }
]
