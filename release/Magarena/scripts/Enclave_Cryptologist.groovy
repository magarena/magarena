[
    new MagicPermanentActivation(
        [
            MagicConditionFactory.CounterAtLeast(MagicCounterType.Level,1),
        ],
        new MagicActivationHints(MagicTiming.Draw),
        "Draw"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            final int amount = source.getCounters(MagicCounterType.Level);
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
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            game.doAction(new DrawAction(player));
            if (event.getRefInt() < 3) {
                game.addEvent(new MagicDiscardEvent(
                    event.getSource(),
                    player
                ));
            }
        }
    }
]
