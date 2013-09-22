
def NecroticSacrifice = new MagicPermanentActivation(
    new MagicActivationHints(MagicTiming.Removal),
    "Destroy"
) {

    @Override
    public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
        return [
            new MagicPayManaCostEvent(source,"{3}"),
            new MagicSacrificeEvent(source)
        ];
    }

    @Override
    public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
        return new MagicEvent(
            source,
            MagicTargetChoice.NEG_TARGET_PERMANENT,
            new MagicDestroyTargetPicker(false),
            this,
            "Destroy target permanent\$."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        event.processTargetPermanent(game,new MagicPermanentAction() {
            public void doAction(final MagicPermanent permanent) {
                game.doAction(new MagicDestroyAction(permanent));
           }
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
            permanent.addAbility(NecroticSacrifice);
        }
    }
]
