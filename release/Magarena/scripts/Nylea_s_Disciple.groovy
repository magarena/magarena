[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "PN gains life equal to PN's devotion to green."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getPlayer().getDevotion(MagicColor.Green);
            game.logAppendMessage(event.getPlayer()," ("+amount+") ");
            game.doAction(new ChangeLifeAction(event.getPlayer(),amount));
        }
    }
]
