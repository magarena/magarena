[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Flash),
        "lose life"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_OPPONENT,
                this,
                "Target opponent\$ loses 1 life. If PN controls a Tezzeret planeswalker, that player loses 3 life instead."
            );
        }
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicPlayer target ->
                final int amount = (event.getPlayer().controlsPermanent(planeswalker(MagicSubType.Tezzeret, Control.You))) ? 3 : 1;
                game.doAction(new ChangeLifeAction(target, -amount));
            });
        }
    }
]

