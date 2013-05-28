[
    new MagicPermanentActivation(
        [
            MagicConditionFactory.ChargeCountersAtLeast(1),
            MagicCondition.CAN_TAP_CONDITION,
        ],
        new MagicActivationHints(MagicTiming.Draw),
        "Draw"
    ) {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            final int amount = source.getCounters(MagicCounterType.Charge);
            final String description = amount >= 3 ?
                    "PN draws a card." :
                    "PN draws a card, then discards a card.";
            return new MagicEvent(
                source,
                amount,
                this,
                description
            );
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            game.doAction(new MagicDrawAction(player,1));
            if (event.getRefInt() < 3) {
                game.addEvent(new MagicDiscardEvent(
                    event.getSource(),
                    player,
                    1,
                    false
                ));
            }
        }
    }
]
