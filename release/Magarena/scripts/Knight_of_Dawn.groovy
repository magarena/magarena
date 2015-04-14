[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Protection"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source,"{W}{W}")];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicColorChoice.ALL_INSTANCE,
                this,
                "SN gains protection from the color\$ of your choice until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new GainAbilityAction(
                event.getPermanent(),
                event.getChosenColor().getProtectionAbility()
            ));
        }
    }
]
