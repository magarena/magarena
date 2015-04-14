[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            final int amount = payedCost.getX();
            return (payedCost.isKicked() && amount > 0) ?
                new MagicEvent(
                    permanent,
                    amount,
                    this,
                    "PN puts "+amount+" 1/1 green Saproling creature tokens onto the battlefield."
                ):
            MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PlayTokensAction(
                    event.getPlayer(),
                    TokenCardDefinitions.get("1/1 green Saproling creature token"),
                    event.getRefInt()
                ));
        }
    }
]
