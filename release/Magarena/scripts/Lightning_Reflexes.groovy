[
    new MagicCardActivation(
        [MagicCondition.NOT_SORCERY_CONDITION],
        new MagicActivationHints(MagicTiming.Pump,true),
        "Flash"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            return source.getCostEvent();
        }
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.POS_TARGET_CREATURE,
                MagicFirstStrikeTargetPicker.create(),
                this,
                "Enchant target creature\$ with SN. " +
                "Sacrifice SN at the beginning of the next cleanup step."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    final MagicPlayCardFromStackAction action = new MagicPlayCardFromStackAction(event.getCardOnStack(),creature);
                    game.doAction(action);
                    game.doAction(new MagicAddTriggerAction(
                        action.getPermanent(),
                        MagicAtEndOfTurnTrigger.Sacrifice
                    ));
                }
            });
        }
    }
]
