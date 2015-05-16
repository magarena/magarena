[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            final MagicPlayer player = permanent.getController();
            NONTOKEN_CREATURE.except(permanent).filter(player) each {
                game.doAction(ChangeStateAction.Set(it, MagicPermanentState.FaceDown))
            }
            game.logAppendMessage(player,player.getName()+" turns all other nontoken creatures face down.");
            return MagicEvent.NONE;
        }
    }
]
