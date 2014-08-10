[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return (upkeepPlayer.getSpellsCastLastTurn() >= 2 || upkeepPlayer.getOpponent().getSpellsCastLastTurn() >= 2) ?
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
