[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_PLAYER,
                payedCost.getTarget(),
                this,
                "Target player\$ discards a number of cards equal to RN's power."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final int amount=event.getRefPermanent().getPower();
                game.logAppendMessage(event.getPlayer(),"("+amount+")");
                game.addEvent(new MagicDiscardEvent(event.getSource(), it, amount));
            });
        }
    }
]
