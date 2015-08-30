[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Pump"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source, "{2}")];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                POS_TARGET_CREATURE_OR_PLAYER,
                MagicPreventTargetPicker.create(),
                this,
                "Prevent the next 1 damage that would be dealt to target creature or player\$ this turn. "+
                "Return SN to its owner's hand at the beginning of the next end step."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                game.doAction(new PreventDamageAction(it, 1));
                game.doAction(new AddTriggerAction(event.getPermanent(), MagicAtEndOfTurnTrigger.Return))
            });
        }
    }
]
