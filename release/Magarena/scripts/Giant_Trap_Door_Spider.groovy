[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Block),
        "Exile"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source), new MagicPayManaCostEvent(source, "{1}{R}{G}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.TARGET_ATTACKING_CREATURE_WITH_FLYING_OPPONENT_CONTROLS,
                this,
                "Exile SN and target creature without flying that's attacking PN\$."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                game.doAction(new MagicRemoveFromPlayAction(event.getPermanent(),MagicLocationType.Exile));
                game.doAction(new MagicRemoveFromPlayAction(it,MagicLocationType.Exile));
            });
        }
    }
]
