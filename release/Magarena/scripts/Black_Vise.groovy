[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPlayer upkeepPlayer) {
            final MagicTarget target = permanent.getChosenTarget();
            return (upkeepPlayer == target) ?
                new MagicEvent(
                    permanent,
                    upkeepPlayer,
                    this,
                    "PN deals X damage to PN " +
                    "where X is the number of cards in his or her hand minus 4."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount = player.getHandSize() - 4;
            if (amount > 0) {
                final MagicDamage damage = new MagicDamage(
                    event.getPermanent(),
                    player,
                    amount
                );
                game.doAction(new MagicDealDamageAction(damage));
            }
        }
    }
]
