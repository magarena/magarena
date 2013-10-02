[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
            final MagicGame game,
            final MagicPermanent permanent,
            final MagicPayedCost payedCost) {
				final MagicPlayer player = permanent.getController();
				if(player.getHandSize() == 0){	
					game.doAction(new MagicSacrificeAction(permanent));
				}else{
					game.addEvent(MagicDiscardEvent.Random(permanent, player, 1));
				}
				return MagicEvent.NONE;
        }
    }
]
