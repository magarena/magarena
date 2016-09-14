[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Prevent"
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
                POS_TARGET_CREATURE,
                MagicPreventTargetPicker.create(),
                this,
                "Prevent the next X damage that would be dealt to target creature\$ this turn, "+
                "where X is the number of Clerics on the battlefield."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,{
                final int X = game.getNrOfPermanents(MagicSubType.Cleric);
                game.doAction(new PreventDamageAction(it,X));
            });
        }
    }
]
