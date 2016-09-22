[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Counter),
        "Counter"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicSacrificeEvent(source)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_SPELL,
                this,
                "Counter target spell\$ unless its controller pays {X}, where X is the number of verse counters on SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game, {
                final int amount = event.getPermanent().getCounters(MagicCounterType.Verse);
                game.addEvent(new MagicCounterUnlessEvent(event.getSource(),it,MagicManaCost.create(amount)));
            });
        }
    }
]
