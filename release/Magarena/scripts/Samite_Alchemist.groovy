[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Prevent"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
		new MagicPayManaCostEvent(source,"{W}{W}"),
                new MagicTapEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.POS_TARGET_CREATURE,
                MagicPreventTargetPicker.create(),
                this,
                "Prevent the next 4 damage that would be dealt to target creature\$ this turn."+
                "Tap that creature. It doesn't untap during your next untap step."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,{
                game.doAction(new MagicPreventDamageAction(it,4));
		game.doAction(new MagicTapAction(it));
		game.doAction(MagicChangeStateAction.Set(
                it,
                MagicPermanentState.DoesNotUntapDuringNext
                ));
            });
        }
    }
]
