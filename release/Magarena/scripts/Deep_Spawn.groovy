[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Put the top two cards of your library into your graveyard?"),
                this,
                "PN may\$ put the top two cards of his or her library into his or her graveyard. If PN doesn't, sacrifice SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes() && event.getPlayer().getLibrary().size() >= 2) {
                game.doAction(new MillLibraryAction(event.getPlayer(),2));
            } else {
                game.doAction(new SacrificeAction(event.getPermanent()));
            }
        }
    }
]
