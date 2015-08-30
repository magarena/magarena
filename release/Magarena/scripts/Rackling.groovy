[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer player) {
            return permanent.isOpponent(player) ?
                new MagicEvent(
                    permanent,
                    player,
                    this,
                    "SN deals X damage to PN, where X is 3 minus the number of cards in his or her hand."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount = 3 - player.getHandSize();
            game.doAction(new DealDamageAction(event.getSource(),player,amount));
        }
    }
]
