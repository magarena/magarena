[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.getOpponent().getLife() > 
                   permanent.getController().getLife() ?
                new MagicEvent(
                    permanent,
                    this,
                    "The player with the most life gains control of SN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getPermanent().getOpponent().getLife() > 
                event.getPermanent().getController().getLife()) {
                game.doAction(new MagicGainControlAction(event.getPermanent().getOpponent(),event.getPermanent()));
            }
        }
    }
]
