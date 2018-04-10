[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                cardOnStack.getOpponent(),
                new MagicOrChoice(
                    MagicChoice.NONE,
                    MagicChoice.NONE
                ),
                cardOnStack.getController(),
                this,
                "Choose one\$ — (1) RN puts a +1/+1 counter on each creature he or she controls and gains 4 life; " +
                "or (2) RN puts a -1/-1 counter on each creature PN controls and SN deals 4 damage to PN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isMode(1)) {
                CREATURE_YOU_CONTROL.filter(event.getRefPlayer()) each {
                    game.doAction(new ChangeCountersAction(event.getRefPlayer(), it, MagicCounterType.PlusOne, 1));
                }
                game.doAction(new ChangeLifeAction(event.getRefPlayer(), 4));
            } else if (event.isMode(2)) {
                CREATURE_YOU_CONTROL.filter(event.getPlayer()) each {
                    game.doAction(new ChangeCountersAction(event.getRefPlayer(), it, MagicCounterType.MinusOne, 1));
                }
                game.doAction(new DealDamageAction(event.getSource(), event.getPlayer(), 4));
            }
        }
    }
]
