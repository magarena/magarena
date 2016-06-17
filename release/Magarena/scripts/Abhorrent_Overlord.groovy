[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "PN puts a number of 1/1 black Harpy creature token with flying onto the battlefield equal to PN's devotion to black. ("+permanent.getController().getDevotion(MagicColor.Black)+")"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getPlayer().getDevotion(MagicColor.Black);
            game.doAction(new PlayTokensAction(
                event.getPlayer(),
                CardDefinitions.getToken("1/1 black Harpy creature token with flying"),
                amount
            ));
        }
    }
]
