[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_PLAYER,
                this,
                "Target player\$ discards a card for each basic land type among lands PN controls."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicPlayer castingPlayer = event.getPlayer()
                final int amount = castingPlayer.getDomain();
                game.logAppendMessage(castingPlayer," ("+amount+")");
                game.addEvent(new MagicDiscardEvent(event.getSource(),it,amount));
            });
        }
    }
]
