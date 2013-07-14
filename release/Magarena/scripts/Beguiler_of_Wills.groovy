[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Control"
    ) {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            final MagicTargetFilter<MagicPermanent> targetFilter = new MagicTargetFilter.MagicPowerTargetFilter(
                MagicTargetFilter.TARGET_CREATURE,
                source.getController().getNrOfPermanents(MagicType.Creature)
            );
            final MagicTargetChoice targetChoice = new MagicTargetChoice(
                targetFilter,
                true,
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
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    if (creature.getPower() <= event.getPlayer().getNrOfPermanents(MagicType.Creature)) {
                        game.doAction(new MagicGainControlAction(event.getPlayer(),creature));
                    }
                }
            });
        }
    }
]
