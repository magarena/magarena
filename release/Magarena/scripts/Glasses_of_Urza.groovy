[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Main),
        "Look"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_PLAYER,
                this,
                "PN looks at target player's\$ hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                MagicPlayer player ->
                game.doAction(new MagicRevealAction(player.getHand()));
            });
        }
    }
]
