[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Pump"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEnchantedEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            final MagicTargetChoice TARGET_OTHER_CREATURE=new MagicTargetChoice(
                CREATURE.except(source.getEnchantedPermanent()),
                MagicTargetHint.Positive,
                "a creature other than " + source.getEnchantedPermanent()
            );
            return new MagicEvent(
                source,
                TARGET_OTHER_CREATURE,
                MagicPumpTargetPicker.create(),
                source.getEnchantedPermanent(),
                this,
                "Target creature other than RN\$ gets +2/+1 until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new ChangeTurnPTAction(it, 2, 1));
            });
        }
    }
]
