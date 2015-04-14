[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return payedCost.isKicked() ?
                new MagicEvent(
                    permanent,
                    payedCost.getKicker(),
                    this,
                    "PN puts a 2/2 green Wolf creature token onto the battlefield for each time SN was kicked. (RN)"
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PlayTokensAction(
                event.getPlayer(),
                TokenCardDefinitions.get("2/2 green Wolf creature token"),
                event.getRefInt()
            ));
        }
    }
]
