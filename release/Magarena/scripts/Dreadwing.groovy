[
new MagicPermanentActivation(
    [
        MagicManaCost.ONE_BLUE_RED.getCondition()
    ],
    new MagicActivationHints(MagicTiming.Pump),
    "Pump") {
    @Override
    public MagicEvent[] getCostEvent(final MagicPermanent source) {
        return [new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.ONE_BLUE_RED)];
    }
    @Override
    public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
        return new MagicEvent(
            source,
            this,
            "SN gets +3/+0 and gains flying until end of turn."
        );
    }
    @Override
    public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object[] choiceResults) {
        game.doAction(new MagicChangeTurnPTAction(event.getPermanent(),3,0));
        game.doAction(new MagicSetAbilityAction(event.getPermanent(),MagicAbility.Flying));
    }
}
]
