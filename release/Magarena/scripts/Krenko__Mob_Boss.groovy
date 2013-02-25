[
    new MagicPermanentActivation(
        [
            MagicCondition.CAN_TAP_CONDITION,
        ],
        new MagicActivationHints(MagicTiming.Flash),
        "Tokens") {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Put X 1/1 red Goblin creature tokens onto the battlefield, where X is the number of Goblins you control."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            final int amt = event.getPlayer().getNrOfPermanentsWithSubType(MagicSubType.Goblin);
            game.doAction(new MagicPlayTokensAction(
                event.getPlayer(), 
                TokenCardDefinitions.get("Goblin1"),
                amt
            ));
        }
    }
]
