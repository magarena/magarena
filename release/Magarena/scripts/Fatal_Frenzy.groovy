[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE_YOU_CONTROL,
                MagicPumpTargetPicker.create(),
                this,
                "Target creature\$ gains trample and gets +X/+0 until end of turn, where X is its power. " +
                "Sacrifice it at the beginning of the next end step."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new GainAbilityAction(it, MagicAbility.Trample));
                game.doAction(new ChangeTurnPTAction(
                    it,
                    it.getPower(),
                    0
                ));
                game.doAction(new AddTriggerAction(it, AtEndOfTurnTrigger.Sacrifice));
            });
        }
    }
]
