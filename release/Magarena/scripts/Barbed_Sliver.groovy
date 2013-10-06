
def BarbedPump = new MagicPermanentActivation(
    new MagicActivationHints(MagicTiming.Pump),
    "Pump"
) {
    @Override
    public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
        return [
            new MagicPayManaCostEvent(source,"{2}")
        ];
    }
    @Override
    public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
        return new MagicEvent(
            source,
            this,
            "SN gets +1/+0 until end of turn. "
        );
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(new MagicChangeTurnPTAction(event.getPermanent(),1,0));
    }
};

[
    new MagicStatic(
        MagicLayer.Ability,
        MagicTargetFilter.TARGET_SLIVER
    ) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            permanent.addAbility(BarbedPump);
        }
    }
]
