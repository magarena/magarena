[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                upkeepPlayer,
                this,
                "SN deals damage to PN equal to the number of Swamps he or she controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount = player.getNrOfPermanents(SWAMP_YOU_CONTROL);
            game.doAction(new DealDamageAction(event.getSource(),player,amount));
            game.logAppendMessage(player," ("+amount+")");
        }
    }
]
