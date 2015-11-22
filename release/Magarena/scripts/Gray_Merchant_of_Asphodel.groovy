[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payed) {
            return new MagicEvent(
                permanent,
                this,
                "Each opponent loses X life, where X is PN's devotion to black. "+
                "PN gains life equal to the life lost this way."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getPlayer().getDevotion(MagicColor.Black);
            final MagicPlayer player=event.getPlayer();
            game.doAction(new ChangeLifeAction(player.getOpponent(),-1*amount));
            game.doAction(new ChangeLifeAction(player, amount));
        }
    }
]
