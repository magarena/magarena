[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Life"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "SN gains 1 life for each elf in play."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
                final int amount = game.getNrOfPermanents(MagicSubType.Elf);
                game.doAction(new MagicChangeLifeAction(event.getPlayer(), amount));
        }
    }
]
