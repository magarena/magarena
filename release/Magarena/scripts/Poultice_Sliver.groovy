
def PoulticeRegen = new MagicPermanentActivation(
    new MagicActivationHints(MagicTiming.Pump),
    "Regen"
) {
    @Override
    public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
        return [
            new MagicTapEvent(source), new MagicPayManaCostEvent(source, "{2}")
        ];
    }
    @Override
    public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
        return new MagicEvent(
            source,
            MagicTargetChoice.Positive("target Sliver"),
            MagicRegenerateTargetPicker.create(),
            this,
            "Regenerate target Sliver\$."
        );
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        event.processTargetPermanent(game, {
            final MagicPermanent creature ->
            game.doAction(new MagicRegenerateAction(creature));
        });
    }
};

[
    new MagicStatic(
        MagicLayer.Ability,
        MagicTargetFilter.TARGET_SLIVER
    ) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            permanent.addAbility(PoulticeRegen);
        }
    }
]
