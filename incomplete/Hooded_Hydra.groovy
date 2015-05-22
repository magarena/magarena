[
    new MagicWhenTurnedFaceUpTrigger(MagicTrigger.REPLACEMENT) { //Still dies before counters can be put on it
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return new MagicEvent(
                permanent,
                this,
                "PN puts five +1/+1 counters on SN."
            )
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeCountersAction(event.getPermanent(), MagicCounterType.PlusOne, 5));
        }
    },
    
    new MagicWhenDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
            return new MagicEvent(
                permanent,
                this,
                "PN puts a 1/1 green Snake creature token onto the battlefield for each +1/+1 counter on SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getPermanent().getCounters(MagicCounterType.PlusOne);
            final MagicPlayer player = event.getPlayer();
            game.logAppendMessage(player, "("+amount+")");
            game.doAction(new PlayTokensAction(
                player,
                CardDefinitions.getToken("1/1 green Snake creature token"),
                amount
            ));
        }
    }
]
