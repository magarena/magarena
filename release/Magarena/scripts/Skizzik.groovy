[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer player) {   
            //At the beginning of the end step, sacrifice Skizzik unless it was kicked.
            if (permanent.isKicked() == false) {
                game.doAction(new MagicAddTriggerAction(permanent, MagicSacrificeAtEnd.create()));
            }
            return MagicEvent.NONE;
        }
    }
]
