[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "PN draws X cards and loses X life, where X is the number of Zombies PN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount = player.getNrOfPermanents(MagicSubType.Zombie);
            game.doAction(new DrawAction(player,amount));
            game.doAction(new ChangeLifeAction(player,-amount));
            game.logAppendX(player,amount);
        }
    }
]
