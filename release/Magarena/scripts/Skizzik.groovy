[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {   
            if (payedCost.isKicked() == false) {
                game.doAction(new MagicAddTriggerAction(permanent, MagicSacrificeAtEnd.create()));
            }
            return MagicEvent.NONE;
        }
    }
]
