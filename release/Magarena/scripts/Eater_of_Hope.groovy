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
            
def EFFECT = MagicRuleEventAction.create("Destroy target creature.");

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
            final MagicTargetChoice targetChoice = MagicTargetChoice.Other("a creature to sacrifice", source);
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
            return EFFECT.getEvent(source);
        }
    }
]
