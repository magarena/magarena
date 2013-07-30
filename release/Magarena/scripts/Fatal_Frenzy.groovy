[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_CREATURE_YOU_CONTROL,
                MagicPumpTargetPicker.create(),
                this,
                "Target creature\$ gains trample and gets +X/+0 until end of turn, where X is its power. " +
                "Sacrifice it at the beginning of the next end step."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicGainAbilityAction(creature, MagicAbility.Trample));
                    game.doAction(new MagicChangeTurnPTAction(
                        creature,
                        creature.getPower(),
                        0
                    ));
                    game.doAction(new MagicAddTriggerAction(creature, MagicAtEndOfTurnTrigger.Sacrifice));
                }
            });
        }
    }
]
