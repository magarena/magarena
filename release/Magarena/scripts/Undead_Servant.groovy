[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "PN puts a 2/2 black Zombie creature token onto the battlefield for each card named "+
                "Undead Servant in his or her graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = cardName("Undead Servant")
                .from(MagicTargetType.Graveyard)
                .filter(event)
                .size();
            final MagicPlayer player = event.getPlayer();
            game.logAppendValue(player, amount);
            game.doAction(new PlayTokensAction(
                player,
                CardDefinitions.getToken("2/2 black Zombie creature token"),
                amount
            ));
        }
    }
]
