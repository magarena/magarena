[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "-Defender"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                    new MagicPayManaCostEvent(source, "{2}{U}{U}")
                ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "SN gets +4/-4 until end of turn. SN can attack this turn as though it didn't have defender."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeTurnPTAction(event.getPermanent,4,-4));
            game.doAction(MagicChangeStateAction.Set(event.getPermanent(),MagicPermanentState.CanAttackWithDefender));
        }
    }
]
