
def SedgeRegen = new MagicPermanentActivation(
    new MagicActivationHints(MagicTiming.Pump),
    "Regen"
) {

    @Override
    public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
        return [new MagicPayManaCostEvent(source,"{B}")];
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
        MagicLayer.ModPT,
        MagicTargetFilter.TARGET_SLIVER
    ) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            if (permanent.getController().controlsPermanent(MagicSubType.Swamp)) {
                pt.add(1,1);
            }
        }
    },
    new MagicStatic(
        MagicLayer.Ability,
        MagicTargetFilter.TARGET_SLIVER
    ) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            permanent.addAbility(SedgeRegen);
        }
    }
]
