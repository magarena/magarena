[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int amount = cardOnStack.getController() == cardOnStack.getGame().getTurnPlayer() && cardOnStack.getGame().isMainPhase() ? 2 : 3;
            return new MagicEvent(
                cardOnStack,
                TARGET_PLAYER,
                amount,
                this,
                "Target player\$ draws four cards, then discards three cards. If PN cast this spell during his or her main phase, instead that player draws four cards, then discards two cards."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final int amount = event.getRefInt();
                game.logAppendValue(event.getPlayer(), amount);
                game.doAction(new DrawAction(it,4));
                game.addEvent(new MagicDiscardEvent(event.getSource(), it, amount));
            })
        }
    }
]
