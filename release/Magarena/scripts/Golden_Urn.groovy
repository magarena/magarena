[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Gain life"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicSacrificeEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN gains life equal to the number of charge counters on SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getPermanent().getCounters(MagicCounterType.Charge);
            game.doAction(new ChangeLifeAction(event.getPlayer(),amount));
        }
    }
]
