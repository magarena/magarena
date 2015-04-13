[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Flying"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{U}{U}")
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_CREATURE_YOU_CONTROL,
                this,
                "Target creature\$ you control gains flying until end of turn. " +
                "Sacrifice it at the beginning of the next end step."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicGainAbilityAction(it, MagicAbility.Flying));
                game.doAction(new AddTriggerAction(it, MagicAtEndOfTurnTrigger.Sacrifice));
            });
        }
    }
]
