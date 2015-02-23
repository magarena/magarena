[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return upkeepPlayer.controlsPermanent(new MagicOtherPermanentTargetFilter(
                MagicTargetFilterFactory.PERMANENT_YOU_CONTROL, permanent)) == false &&
                upkeepPlayer.getHandSize() == 0 ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN wins the game."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicLoseGameAction(event.getPlayer().getOpponent()));
        };
    }
]
