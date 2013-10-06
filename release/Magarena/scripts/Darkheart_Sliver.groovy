
def DarkheartSacrifice = new MagicPermanentActivation(
    new MagicActivationHints(MagicTiming.Removal),
    "Life"
) {
    @Override
    public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
        return [new MagicSacrificeEvent(source)];
    }
    @Override
    public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
        return new MagicEvent(
            source,
            this,
            "PN gains 3 life."
        );
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(new MagicChangeLifeAction(event.getPlayer(),3));
    }
};

[
    new MagicStatic(
        MagicLayer.Ability,
        MagicTargetFilter.TARGET_SLIVER
    ) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            permanent.addAbility(DarkheartSacrifice);
        }
    }
]
