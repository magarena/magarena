def OPPONENT_WHO_HAS_MORE_LIFE = new MagicPlayerFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPlayer target) {
        return target.isEnemy(player) && target.getLife() > player.getLife();
    }
};

def TARGET_OPPONENT_WHO_HAS_MORE_LIFE = new MagicTargetChoice(
    OPPONENT_WHO_HAS_MORE_LIFE,
    "target opponent who has more life than you"
);

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{R}"),
                new MagicTapEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_OPPONENT_WHO_HAS_MORE_LIFE,
                this,
                "SN deals 2 damage to target opponent\$ who has more life than PN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.doAction(new DealDamageAction(event.getSource(),it,2));
            });
        }
    }
]
