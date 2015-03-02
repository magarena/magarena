[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return (upkeepPlayer.getNrOfPermanents(MagicSubType.Plains)>0) ?
				new MagicEvent(
					permanent,
					upkeepPlayer,
					this,
					"PN gains 1 life."
				):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
			if (event.getPlayer().getNrOfPermanents(MagicSubType.Plains)>0) {
				game.doAction(new MagicChangeLifeAction(event.getPlayer(), 1));
			}
        }
    }
]
