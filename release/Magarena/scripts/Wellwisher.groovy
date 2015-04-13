[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "+Life"
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
                "PN gains 1 life for each Elf on the battlefield."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = game.getNrOfPermanents(MagicSubType.Elf);
            game.doAction(new ChangeLifeAction(event.getPlayer(), amount));
        }
    }
]
