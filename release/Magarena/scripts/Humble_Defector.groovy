[
    new MagicPermanentActivation(
        [MagicCondition.YOUR_TURN_CONDITION],
        new MagicActivationHints(MagicTiming.Draw),
        "Draw"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_OPPONENT,
                this,
                "PN draws two cards. Target opponent\$ gains control of SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DrawAction(event.getPlayer(),2));
            event.processTargetPlayer(game, {
                game.doAction(new GainControlAction(it,event.getPermanent()));
            });
        }
    }
]
