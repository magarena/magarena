def Control_Since_Last_Turn = new MagicCondition() {
    public boolean accept(final MagicSource source) {
        final MagicPermanent permanent = (MagicPermanent)source;
        return permanent.hasState(MagicPermanentState.Summoned) == false;
    }
}

[
    new MagicPermanentActivation(
        [Control_Since_Last_Turn],
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source, "{2}")];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_CREATURE_OR_PLAYER,
                new MagicDamageTargetPicker(1),
                this,
                "SN deals 1 damage to target creature or player.\$ Destroy SN at the beginning of the next end step."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                game.doAction(new DealDamageAction(event.getSource(), it, 1));
                game.doAction(new AddTriggerAction(event.getPermanent(), AtEndOfTurnTrigger.Destroy))
            });
        }
    }
]
