def SNOW_MOUNTAIN = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.hasType(MagicType.Snow) &&
                   target.hasSubType(MagicSubType.Mountain);
        }
}

def SNOW_MOUNTAIN_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            return source.getController().controlsPermanent(SNOW_MOUNTAIN);
        }
    };

[
    new MagicPermanentActivation(
        [SNOW_MOUNTAIN_CONDITION],
        new MagicActivationHints(MagicTiming.Pump),
        "Flying"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPlayAbilityEvent(source),
                new MagicPayManaCostEvent(source, "{1}{R}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "SN gets +2/+0 and gains flying. PN sacrifices it at the beginning of the next end step."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent();
            game.doAction(new ChangeTurnPTAction(permanent, 2, 0));
            game.doAction(new GainAbilityAction(permanent, MagicAbility.Flying));
            game.doAction(new AddTurnTriggerAction(permanent, AtEndOfTurnTrigger.Sacrifice))
        }
    }
]
