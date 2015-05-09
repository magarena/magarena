[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "PN puts a 2/2 white Cat creature token onto the " +
                "battlefield for each Equipment attached to SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent();
            game.doAction(new PlayTokensAction(
                event.getPlayer(),
                CardDefinitions.getToken("2/2 white Cat creature token"),
                permanent.getEquipmentPermanents().size()
            ));
        }
    }
]
