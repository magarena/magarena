[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_CREATURE,
                new MagicWeakenTargetPicker(amount,amount),
                payedCost.getX(),
                this,
                "Target creature\$ gets -RN/-RN until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    final int X = event.getRefInt();
                    game.doAction(new MagicChangeTurnPTAction(creature,-X,-X));
                }
            });
        }
    }
]
