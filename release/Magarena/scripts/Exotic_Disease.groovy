[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int amount = cardOnStack.getController().getDomain();
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_PLAYER,
                this,
                "Target player\$ loses X life and PN gains X life, where X is the number of basic land types among lands PN controls."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicPlayer castingPlayer = event.getPlayer()
                final int amount = castingPlayer.getDomain();
                game.logAppendMessage(castingPlayer," (X="+amount+")");
                game.doAction(new ChangeLifeAction(it,-amount));
                game.doAction(new ChangeLifeAction(castingPlayer,amount));
            });
        }
    }
]
