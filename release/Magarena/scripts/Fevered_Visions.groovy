[
    new AtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer player) {
            return new MagicEvent(
                permanent,
                player,
                this,
                "PN draws a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final MagicPermanent permanent = event.getPermanent();
            game.doAction(new DrawAction(player));
            if (player.isEnemy(permanent) && player.getHandSize() >= 4) {
                game.logAppendMessage(player, String.format(
                    "%s deals 2 damage to ${player}",
                    MagicMessage.getCardToken(permanent)
                ));
                game.doAction(new DealDamageAction(permanent, player, 2));
            }
        }
    }
]
