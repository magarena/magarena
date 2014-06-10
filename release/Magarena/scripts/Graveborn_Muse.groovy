[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN draw X cards and PN lose X life, where X is the number of Zombies PN control."
                ):
                MagicEvent.NONE;
        }
		
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
			final int amount = player.getNrOfPermanents(MagicSubType.Zombie);
            game.doAction(new MagicDrawAction(player,amount));
            game.doAction(new MagicChangeLifeAction(player,-amount));
        }
    }
]
