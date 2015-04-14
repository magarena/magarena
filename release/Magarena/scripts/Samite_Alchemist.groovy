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
                TARGET_CREATURE_YOU_CONTROL,
                MagicPreventTargetPicker.create(),
                this,
                "Prevent the next 4 damage that would be dealt this turn to target creature you control\$."+
                "Tap that creature. It doesn't untap during your next untap step."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,{
                game.doAction(new PreventDamageAction(it,4));
                game.doAction(new MagicTapAction(it));
                game.doAction(ChangeStateAction.Set(
                    it,
                    MagicPermanentState.DoesNotUntapDuringNext
                ));
            });
        }
    }
]
