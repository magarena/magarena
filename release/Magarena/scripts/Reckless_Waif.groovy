[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return (game.getSpellsPlayedLastTurn() == 0) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN transforms SN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            game.doAction(new MagicTransformAction(event.getPermanent()));
        }
    }
]
