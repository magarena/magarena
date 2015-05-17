[
    new MagicWhenOtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
            return (died.isCreature()) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(NEG_TARGET_PLAYER),
                    this,
                    "PN may\$ have SN deal damage to target player\$ equal to the number of cards in that player's hand."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPlayer(game, {
                    final int amount = it.getHandSize();
                    game.logAppendMessage(event.getPlayer(), "("+amount+")");
                    game.doAction(new DealDamageAction(event.getPermanent(), it, it.getHandSize()));
                });
            }
        }
    }
]
