
def ClotRegen = new MagicPermanentActivation(
    new MagicActivationHints(MagicTiming.Pump),
    "Regen"
) {

    @Override
    public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
        return [new MagicPayManaCostEvent(source,"{2}")];
    }

   @Override
    public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
        return new MagicEvent(
            source,
            this,
            "Regenerate SN."
        );
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(new MagicRegenerateAction(event.getPermanent()));
    }
};

[
    new MagicStatic(
        MagicLayer.Ability,
        MagicTargetFilter.TARGET_SLIVER
    ) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            permanent.addAbility(ClotRegen);
        }
    }
]
