def OPPONENT_WITH_TWO_MORE_CARDS = new MagicPlayerFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPlayer target) {
        return target.isEnemy(player) && target.getHandSize() >= player.getHandSize() + 2;
    }
};

def TARGET_OPPONENT_WITH_TWO_MORE_CARDS = new MagicTargetChoice(
    OPPONENT_WITH_TWO_MORE_CARDS,
    "target opponent who has at least two more cards in hand than you"
);

def effect = MagicRuleEventAction.create("Draw a card.");

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Draw),
        "Draw"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{U}"),
                new MagicTapEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_OPPONENT_WITH_TWO_MORE_CARDS,
                effect.getAction(),
                "Draw a card."
            );
        }
    }
]
