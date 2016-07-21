[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.getOpponent().getNrOfPermanents(MagicType.Creature) > permanent.getController().getNrOfPermanents(MagicType.Creature) ?
                new MagicEvent(
                    permanent,
                    this,
                    "The player with the most creatures gains control of SN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getPlayer().getOpponent().getNrOfPermanents(MagicType.Creature) > event.getPlayer().getNrOfPermanents(MagicType.Creature)) {
                game.doAction(new GainControlAction(event.getPlayer().getOpponent(), event.getPermanent()));
            }
        }
    }
]
