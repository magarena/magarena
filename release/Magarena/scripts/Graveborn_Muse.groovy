[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN draws X cards and loses X life, where X is the number of Zombies PN controls."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getPlayer().getNrOfPermanents(MagicSubType.Zombie);
            final MagicPlayer player = event.getPlayer();
            game.doAction(new MagicDrawAction(player,amount));
            game.doAction(new MagicChangeLifeAction(player,-amount));
            game.logAppendMessage(player," (X="+amount+")");
        }
    }
]
