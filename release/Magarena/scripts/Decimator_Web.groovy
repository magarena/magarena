[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Draw),
        "Mill"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{4}"),
                new MagicTapEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_OPPONENT,
                this,
                "Target opponent\$ loses 2 life, gets a poison counter, then puts the top six cards of his or her library into his or her graveyard."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.doAction(new ChangeLifeAction(it, -2));
                game.doAction(new ChangeCountersAction(it, MagicCounterType.Poison, 1));
                game.doAction(new MillLibraryAction(it, 6));
            });
        }
    }
]
