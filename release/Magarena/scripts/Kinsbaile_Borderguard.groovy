[
    new MagicWhenDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
            return new MagicEvent(
                permanent,
                this,
                "PN puts a 1/1 white Kithkin Soldier creature token onto the battlefield for each counter on SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            int amount = 0;
            final MagicPermanent permanent = event.getPermanent();
            for (final MagicCounterType counterType : MagicCounterType.values()) {
                if (permanent.hasCounters(counterType)) {
                    amount+=permanent.getCounters(counterType);
                }
            }
            final MagicPlayer player = event.getPlayer();
            game.logAppendMessage(player, "("+amount+")");
            game.doAction(new PlayTokensAction(
                player,
                CardDefinitions.getToken("1/1 white Kithkin Soldier creature token"),
                amount
            ));
        }
    }
]
