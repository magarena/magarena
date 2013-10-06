
def MnemonicSacrifice = new MagicPermanentActivation(
    new MagicActivationHints(MagicTiming.Pump),
    "Draw"
) {

    @Override
    public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
        return [
            new MagicPayManaCostEvent(source,"{2}"),
            new MagicSacrificeEvent(source)
        ];
    }

    @Override
    public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
        return new MagicEvent(
            source,
            this,
            "PN draws a card."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(new MagicDrawAction(event.getPlayer(),1));
    }
};

[
    new MagicStatic(
        MagicLayer.Ability,
        MagicTargetFilter.TARGET_SLIVER
    ) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            permanent.addAbility(MnemonicSacrifice);
        }
    }
]
