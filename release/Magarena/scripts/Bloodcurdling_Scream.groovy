[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int amount = payedCost.getX();
            return new MagicEvent(
                cardOnStack,
                POS_TARGET_CREATURE,
                MagicPumpTargetPicker.create(),
                amount,
                this,
                "Target creature\$ gets +RN/+0 until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getRefInt();
            event.processTargetPermanent(game, {
                game.doAction(new MagicChangeTurnPTAction(it,amount,0));
            });
        }
    }
]
