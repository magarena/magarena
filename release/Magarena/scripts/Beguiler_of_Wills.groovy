[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Control"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            final MagicTargetChoice targetChoice = new MagicTargetChoice(
                new MagicPTTargetFilter(
                    CREATURE,
                    source.getController().getNrOfPermanents(MagicType.Creature)
                ),
                MagicTargetHint.Negative,
                "target creature to gain control of"
            );
            return new MagicEvent(
                source,
                targetChoice,
                MagicExileTargetPicker.create(),
                this,
                "Gain control of target creature\$ with power less " +
                "than or equal to the number of creatures you control."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                if (it.getPower() <= event.getPlayer().getNrOfPermanents(MagicType.Creature)) {
                    game.doAction(new GainControlAction(event.getPlayer(),it));
                }
            });
        }
    }
]
