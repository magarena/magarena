[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "PN puts a 1/1 colorless Spirit creature token onto the battlefield for each Shrine he or she control."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int size = event.getPlayer().getNrOfPermanents(MagicSubType.Shrine);
            game.doAction(new PlayTokensAction(
                event.getPlayer(),
                TokenCardDefinitions.get("1/1 colorless Spirit creature token"),
                size
            ));
        }
    }
]
