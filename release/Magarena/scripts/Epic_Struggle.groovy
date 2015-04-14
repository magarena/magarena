[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return upkeepPlayer.getNrOfPermanents(MagicType.Creature) >= 20 ?
                new MagicEvent(
                    permanent,
                    this,
                    "If PN controls twenty or more creatures, PN wins the game."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getPlayer().getNrOfPermanents(MagicType.Creature) >= 20) {
                game.doAction(new LoseGameAction(event.getPlayer().getOpponent()));
            }
        };
    }
]
