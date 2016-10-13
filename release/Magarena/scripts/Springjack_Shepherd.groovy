[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "PN creates a 0/1 white Goat creature token for each white mana symbol in the mana costs of permanents he or she controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getPlayer().getDevotion(MagicColor.White);
            game.doAction(new PlayTokensAction(
                event.getPlayer(),
                CardDefinitions.getToken("0/1 white Goat creature token"),
                amount
            ));
        }
    }
]
