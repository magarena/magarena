[
    new MagicAtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.getController().controlsPermanent(MagicSubType.Goblin) == false ?
                new MagicEvent(
                    permanent,
                    this,
                    "If PN controls no Goblins, sacrifice SN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getPlayer().controlsPermanent(MagicSubType.Goblin) == false) {
                game.doAction(new MagicSacrificeAction(event.getPermanent()));
            }
        }
    }
]
