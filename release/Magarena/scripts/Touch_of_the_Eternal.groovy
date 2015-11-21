[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            new MagicEvent(
                permanent,
                this,
                "PN's life total becomes the number of permanents he or she controls."
            )
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount = player.getNrOfPermanents();
            game.doAction(new ChangeLifeAction(player, amount - player.getLife()));
            game.logAppendValue(player, amount);
        }
    }
]
