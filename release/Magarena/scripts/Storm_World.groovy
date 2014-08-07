[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                    permanent,
                    upkeepPlayer,
                    this,
                    "SN deals X damage to PN, where X is 4 minus the number of cards in his or her hand."
                );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount = 4 - player.getHandSize();
            final MagicDamage damage = new MagicDamage(
                event.getSource(),
                player,
                amount
            );
            game.doAction(new MagicDealDamageAction(damage));
        }
    }
]
