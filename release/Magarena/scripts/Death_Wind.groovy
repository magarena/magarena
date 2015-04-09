[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int amount = payedCost.getX();
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                new MagicWeakenTargetPicker(amount,amount),
                amount,
                this,
                "Target creature\$ gets -RN/-RN until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final int X = event.getRefInt();
                game.doAction(new MagicChangeTurnPTAction(it,-X,-X));
            });
        }
    }
]
