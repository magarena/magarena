[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                this,
                "Target creature\$ deals damage to itself equal to its power."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final int amount = it.getPower();
                game.logAppendValue(event.getPlayer(),amount);
                game.doAction(new DealDamageAction(it,it,amount));
            });
        }
    }
]
