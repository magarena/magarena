[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "PN gains X life, where X is the number of cards in his or her hand minus 4."
            ); 
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount = Math.max(0,player.getHandSize() - 4);
            game.logAppendX(player,amount);
            game.doAction(new ChangeLifeAction(player,amount));
        }
    }
]
