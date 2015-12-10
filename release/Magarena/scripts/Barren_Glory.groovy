[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            final MagicTargetFilter<MagicPermanent> other = PERMANENT_YOU_CONTROL.except(permanent);
            if (upkeepPlayer.controlsPermanent(other) == false && upkeepPlayer.getHandSize() == 0) {
                game.logMessage(upkeepPlayer, "${upkeepPlayer.getName()} wins the game");
                game.doAction(new LoseGameAction(upkeepPlayer.getOpponent()));
            }
            return MagicEvent.NONE;
        }
    }
]
