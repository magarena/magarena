[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_SPELL,
                cardOnStack.getController(),
                this,
                "SN deals damage to target spell's\$ controller equal to that spell's converted mana cost."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game, {
                final int amount = it.getConvertedCost();
                game.logAppendMessage(event.getPlayer(),"("+amount+")");
                game.doAction(new DealDamageAction(event.getSource(), it.getController(), amount));
            });
        }
    }
]
