[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Attack),
        "Unblockable"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.POS_TARGET_MERFOLK_CREATURE,
                MagicUnblockableTargetPicker.create(),
                this,
                "Target Merfolk creature\$ is unblockable this turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicGainAbilityAction(creature,MagicAbility.Unblockable));
                }
            });
        }
    }
]
