def action = {
    final MagicGame game, final MagicEvent event ->
    game.doAction(new GainAbilityAction(
        event.getRefPermanent(),
        event.getChosenColor().getProtectionAbility()
    ));
}

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Protection"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                    new MagicPayManaCostEvent(source,"{2}{W}{W}"),
                    new MagicUntapEvent(source)
                ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                POS_TARGET_CREATURE,
                this,
                "Target creature\$ gains protection from the color of its controller's choice until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    it.getController(),
                    MagicColorChoice.ALL_INSTANCE,
                    it,
                    action,
                    "Chosen color\$."
                ));
            });
        }
    }
]
