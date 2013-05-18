[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int amount = payedCost.getX();
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_CREATURE,
                new MagicWeakenTargetPicker(amount,amount),
                this,
                "Target creature\$ gets -" + amount + "/-" + amount + " until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    final int X = event.getCardOnStack().getX();
                    game.doAction(new MagicChangeTurnPTAction(creature,-X,-X));
                }
            });
        }
    }
]
