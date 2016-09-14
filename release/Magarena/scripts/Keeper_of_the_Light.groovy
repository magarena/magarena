def OPPONENT_WHO_HAS_MORE_LIFE = new MagicPlayerFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPlayer target) {
        return target.isEnemy(player) && target.getLife() > player.getLife();
    }
};

def TARGET_OPPONENT_WHO_HAS_MORE_LIFE = new MagicTargetChoice(
    OPPONENT_WHO_HAS_MORE_LIFE,
    "target opponent who has more life than you"
);

def effect = MagicRuleEventAction.create("You gain 3 life.");

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "+Life"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{W}"),
                new MagicTapEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_OPPONENT_WHO_HAS_MORE_LIFE,
                effect.getAction(),
                "PN gains 3 life."
            );
        }
    }
]
