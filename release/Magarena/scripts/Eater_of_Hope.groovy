def TWO_OTHER_CREATURES_CONDITION = new MagicCondition() {
    public boolean accept(final MagicSource source) {
        final MagicTargetFilter<MagicPermanent> filter =new MagicOtherPermanentTargetFilter(
            MagicTargetFilterFactory.CREATURE_YOU_CONTROL, 
            (MagicPermanent)source
        );
        final MagicGame game = source.getGame();
        final MagicPlayer player = source.getController();
        return game.filterPermanents(player, filter).size() >= 2;
    }
};

[
    new MagicPermanentActivation(
        [
            TWO_OTHER_CREATURES_CONDITION,
        ],
        new MagicActivationHints(MagicTiming.Removal),
        "Destroy"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            final MagicTargetChoice targetChoice = new MagicTargetChoice(
                new MagicOtherPermanentTargetFilter(
                    MagicTargetFilterFactory.CREATURE_YOU_CONTROL,
                    source
                ),
                MagicTargetHint.None,
                "a creature other than " + source + " to sacrifice"
            );
            return [
                new MagicPayManaCostEvent(source,"{2}{B}"),
                new MagicSacrificePermanentEvent(
                    source,
                    targetChoice
                ),
                new MagicSacrificePermanentEvent(
                    source,
                    targetChoice
                )
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_CREATURE,
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target creature\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicDestroyAction(it));
            });
        }
    }
]
