[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int amount = cardOnStack.getController() == cardOnStack.getGame().getTurnPlayer() && cardOnStack.getGame().isMainPhase() ? 4 : 2;
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_PLAYER,
                amount,
                this,
                "Target player\$ discards two cards. If PN cast this spell during his or her main phase, that player discards four cards instead."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final int amount = event.getRefInt();
                game.logAppendValue(event.getPlayer(), amount);
                game.addEvent(new MagicDiscardEvent(event.getSource(), it, amount));
            });
        }
    }
]
