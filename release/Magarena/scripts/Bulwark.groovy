[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            new MagicEvent(
                permanent,
                TARGET_OPPONENT,
                this,
                "SN deals X damage to target opponent\$, where X is the number of cards in PN's hand minus the number of cards in RN's hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final int amount = event.getPlayer().getHandSize() - it.getHandSize();
                game.doAction(new DealDamageAction(event.getPermanent(),it,amount));
            });
        }
    }
]
