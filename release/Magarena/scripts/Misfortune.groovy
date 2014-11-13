[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                cardOnStack.getOpponent(),
                new MagicOrChoice(
                    MagicTargetChoice.NONE,
                    MagicTargetChoice.NONE
                ),
                this,
                "Choose one\$ - your opponent puts a +1/+1 counter on each creature he or she controls and gains 4 life; " +
                "or your opponent puts a -1/-1 counter on each creature you control and SN deals 4 damage to you."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isMode(1)) {
                final Collection<MagicPermanent> targets = event.getPlayer().getOpponent().filterPermanents(MagicTargetFilterFactory.CREATURE);
                for (final MagicPermanent creature : targets) {         
                game.doAction(new MagicChangeCountersAction(creature, MagicCounterType.PlusOne, 1));
                }
                game.doAction(new MagicChangeLifeAction(event.getPlayer().getOpponent(), 4));
            } else if (event.isMode(2)) {
                final Collection<MagicPermanent> targets = event.getPlayer().filterPermanents(MagicTargetFilterFactory.CREATURE);
                for (final MagicPermanent creature : targets) {         
                game.doAction(new MagicChangeCountersAction(creature, MagicCounterType.MinusOne, 1));
                }
                game.doAction(new MagicDealDamageAction(event.getSource(), event.getPlayer(), 4));
            }
        }
    }
]
