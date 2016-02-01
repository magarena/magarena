[
    new EntersBattlefieldTrigger(MagicTrigger.REPLACEMENT) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
            final MagicPlayer player = permanent.getController();
            permanentName("Sheltered Valley", Control.You).except(permanent).filter(player) each {
                game.doAction(new SacrificeAction(it));
                game.logAppendMessage(player, "(${it.getName()}) is sacrificed.");
            }
            return MagicEvent.NONE;
        }
    }
]
