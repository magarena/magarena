[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Remove"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicSacrificeEvent(source)];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.TARGET_PERMANENT,
                MagicCountersTargetPicker.getInstance(),
                this,
                "Remove all counters from target permanent\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent permanent) {
                    for (final MagicCounterType counterType : MagicCounterType.values()) {
                        final int amount=permanent.getCounters(counterType);
                        if (amount>0) {
                            game.doAction(new MagicChangeCountersAction(permanent,counterType,-amount,true));
                        }
                    }
                }
            });
        }
    }
]
