[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_OPPONENT,
                payedCost.getTarget(),
                this,
                "SN deals damage to target opponent\$ equal to RN's power."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final int amount=event.getRefPermanent().getPower();
                game.logAppendMessage(event.getPlayer(),"("+amount+")");
                game.doAction(new DealDamageAction(event.getSource(), it, amount));
            });
        }
    }
]
