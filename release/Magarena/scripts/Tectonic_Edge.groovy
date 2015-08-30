def OPP_FOUR_LANDS_CONDITION=new MagicCondition() {
    public boolean accept(final MagicSource source) {
        return source.getOpponent().getNrOfPermanents(MagicType.Land)>=4;
    }
};

[
    new MagicPermanentActivation(
        [OPP_FOUR_LANDS_CONDITION],
        new MagicActivationHints(MagicTiming.Removal),
        "Destroy"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicSacrificeEvent(source),
                new MagicTapEvent(source), new MagicPayManaCostEvent(source, "{1}")
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_NONBASIC_LAND,
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target nonbasic land\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new DestroyAction(it));
            });
        }
    }
]
