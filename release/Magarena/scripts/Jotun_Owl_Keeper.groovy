[
    new MagicWhenLeavesPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicRemoveFromPlayAction act) {
            final int amount = permanent.getCounters(MagicCounterType.Age);
            return (act.isPermanent(permanent) && amount > 0) ?
                new MagicEvent(
                    permanent,
                    amount,
                    this,
                    amount > 1 ?
                        "PN puts " + amount + " 1/1 white Bird creature tokens with flying onto the battlefield." :
                        "PN puts a 1/1 white Bird creature token with flying onto the battlefield."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicPlayTokensAction(
                event.getPlayer(),
                TokenCardDefinitions.get("1/1 white Bird creature token with flying"),
                event.getRefInt()
            ));
        }
    }
]
