[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "-Defender"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                    new MagicPayManaCostEvent(source, "{3}"),
                    new MagicPlayAbilityEvent(source)
                ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "SN gets +3/-1 until end of turn. SN can attack this turn as though it didn't have defender."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeTurnPTAction(event.getPermanent,3,-1));
            game.doAction(MagicChangeStateAction.Set(event.getPermanent(),MagicPermanentState.CanAttackWithDefender));
        }
    }
]
