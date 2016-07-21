[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.getOpponent().getNrOfPermanents(MagicSubType.Wizard) > permanent.getController().getNrOfPermanents(MagicSubType.Wizard) ?
                new MagicEvent(
                    permanent,
                    this,
                    "The player with the most Wizards gains control of SN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getPlayer().getOpponent().getNrOfPermanents(MagicSubType.Wizard) > event.getPlayer().getNrOfPermanents(MagicSubType.Wizard)) {
                game.doAction(new GainControlAction(event.getPlayer().getOpponent(), event.getPermanent()));
            }
        }
    }
]
