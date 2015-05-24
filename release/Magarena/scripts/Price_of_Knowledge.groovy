[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer player) {
            return permanent.isOpponent(player) ?
                new MagicEvent(
                    permanent,
                    player,
                    this,
                    "SN deals damage to PN equal to the number of cards in that player's hand."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getPlayer().getHandSize();
            game.logAppendValue(event.getPlayer(),amount);
            game.doAction(new DealDamageAction(event.getSource(), event.getPlayer(), amount));
        }
    }
]
