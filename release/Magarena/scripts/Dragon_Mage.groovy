[
    new MagicWhenSelfCombatDamagePlayerTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return new MagicEvent(
                permanent,
                damage.getTargetPlayer(),
                this,
                "Each player discards his or her hand, then draws seven cards."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPlayer player : game.getAPNAP()) {
                final MagicCardList hand = new MagicCardList(player.getHand());
                for (final MagicCard card : hand) {
                    game.doAction(new DiscardCardAction(player,card));
                }
            }
            for (final MagicPlayer player : game.getAPNAP()) {
                game.doAction(new DrawAction(player, 7));
            }
        }
    }
]
