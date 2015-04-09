[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int amount = cardOnStack.getController().getDomain();
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE_OR_PLAYER,
                new MagicDamageTargetPicker(amount),
                this,
                "SN deals X damage to target creature or player\$, where X is the number of basic land types among lands PN controls."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                final MagicPlayer castingPlayer = event.getPlayer()
                final int amount = castingPlayer.getDomain();
                game.logAppendMessage(castingPlayer," (X="+amount+")");
                game.doAction(new MagicDealDamageAction(event.getSource(),it,amount));
            });
        }
    }
]
