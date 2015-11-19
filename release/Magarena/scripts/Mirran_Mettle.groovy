[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                POS_TARGET_CREATURE,
                MagicPumpTargetPicker.create(),
                this,
                "Target creature\$ gets +2/+2 until end of turn. " +
                "If PN controls three or more artifacts, it gets +4/+4 instead."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final int amount = MagicCondition.METALCRAFT_CONDITION.accept(event.getSource()) ? 4 : 2;
                game.doAction(new ChangeTurnPTAction(it,amount,amount));
            });
        }
    }
]
