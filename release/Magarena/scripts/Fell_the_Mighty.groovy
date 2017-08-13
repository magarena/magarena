[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE,
                this,
                "Destroy all creatures with power greater than target creature's\$ power."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final int amount = it.getPower();
				game.logAppendValue(event.getPlayer(), amount);
				game.doAction(new DestroyAction(
					new MagicPTTargetFilter(
						CREATURE,
						Operator.GREATER_THAN,
						amount
					).filter(event)
				));
            });
        }
    }
]
