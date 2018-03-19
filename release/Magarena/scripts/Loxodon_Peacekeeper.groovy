[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.getOpponent().getLife() < permanent.getController().getLife() ?
                new MagicEvent(
                    permanent,
                    this,
                    "The player with the lowest life gains control of SN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getPlayer().getOpponent().getLife() < event.getPlayer().getLife()) {
                game.doAction(new GainControlAction(event.getPlayer().getOpponent(), event.getPermanent()));
            }
        }
    }
]
