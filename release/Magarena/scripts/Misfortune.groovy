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
                "Choose one\$ - RN puts a +1/+1 counter on each creature he or she controls and gains 4 life; " +
                "or RN puts a -1/-1 counter on each creature PN controls and SN deals 4 damage to PN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isMode(1)) {
                final Collection<MagicPermanent> targets = event.getRefPlayer().filterPermanents(CREATURE);
                for (final MagicPermanent creature : targets) {         
                    game.doAction(new MagicChangeCountersAction(creature, MagicCounterType.PlusOne, 1));
                }
                game.doAction(new MagicChangeLifeAction(event.getRefPlayer(), 4));
            } else if (event.isMode(2)) {
                final Collection<MagicPermanent> targets = event.getPlayer().filterPermanents(CREATURE);
                for (final MagicPermanent creature : targets) {         
                    game.doAction(new MagicChangeCountersAction(creature, MagicCounterType.MinusOne, 1));
                }
                game.doAction(new MagicDealDamageAction(event.getSource(), event.getPlayer(), 4));
            }
        }
    }
]
