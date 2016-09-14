[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.None),
        "Mill"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent permanent,final MagicPayedCost payedCost) {
            final int amount = permanent.getCounters(MagicCounterType.Charge)
            return new MagicEvent(
                permanent,
                TARGET_PLAYER,
                amount,
                this,
                "Target player\$ puts the top X cards of his or her library into his or her graveyard, where X is the number of charge counters on SN (X="+amount+")."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.doAction(new MillLibraryAction(it,event.getRefInt()));
            });
        }
    }
]
