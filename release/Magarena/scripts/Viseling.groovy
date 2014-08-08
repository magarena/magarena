[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer player) {
            return permanent.isOpponent(player) ?
                new MagicEvent(
                    permanent,
                    player,
                    this,
                    "SN deals X damage to PN, where X is the number of cards in his or her hand minus 4."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount = player.getHandSize() - 4;
            final MagicDamage damage = new MagicDamage(
                event.getSource(),
                player,
                amount
            );
            game.doAction(new MagicDealDamageAction(damage));
        }
    }
]
