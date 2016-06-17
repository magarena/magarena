[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "PN puts a number of 1/0 blue Elemental creature token onto the battlefield equal to PN's devotion to blue. ("+permanent.getController().getDevotion(MagicColor.Blue)+")"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getPlayer().getDevotion(MagicColor.Blue);
            game.doAction(new PlayTokensAction(
                event.getPlayer(),
                CardDefinitions.getToken("1/0 blue Elemental creature token"),
                amount
            ));
        }
    }
]
