[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
            return new MagicEvent(
                    permanent,
                    player,
                    this,
                    "Each player gets a poison counter.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            game.doAction(new MagicChangePoisonAction(game.getPlayer(0),1));
            game.doAction(new MagicChangePoisonAction(game.getPlayer(1),1));
        }
    }
]
