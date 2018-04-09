[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                TARGET_OPPONENT,
                this,
                "PN puts a +1/+1 counter on SN for each card in his or her hand, "+
                "then removes a +1/+1 counter from SN for each card in target opponent's hand.\$"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.doAction(new ChangeCountersAction(
                    event.getPlayer(),
                    event.getPermanent(),
                    MagicCounterType.PlusOne,
                    event.getPlayer().getHandSize()
                ));
                game.doAction(new ChangeCountersAction(
                    event.getPlayer(),
                    event.getPermanent(),
                    MagicCounterType.PlusOne,
                    -it.getHandSize()
                ))
            });
        }
    }
]
