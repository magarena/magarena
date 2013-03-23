[
new MagicPermanentActivation(
    [
        MagicCondition.TAPPED_CONDITION,
        MagicCondition.YOUR_UPKEEP_CONDITION,
        MagicConditionFactory.ManaCost("{9}"),
        new MagicSingleActivationCondition()
    ],
    new MagicActivationHints(MagicTiming.Tapping),
    "Untap") {
    @Override
    public MagicEvent[] getCostEvent(final MagicPermanent source) {
        return [new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.create("{9}"))];
    }
    @Override
    public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
        return new MagicEvent(
            source,
            this,
            "Untap SN."
        );
    }
    @Override
    public void executeEvent(
            final MagicGame game,
            final MagicEvent event) {
        game.doAction(new MagicUntapAction(event.getPermanent()));
    }
}
]
