[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return upkeepPlayer.getNrOfPermanents(MagicType.Land) >= 6 ?
                new MagicEvent(
                    permanent,
                    this,
                    "If PN controls six or more lands, PN puts a 5/5 red Dragon creature token with flying onto the battlefield."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getPlayer().getNrOfPermanents(MagicType.Land) >= 6) {
                game.doAction(new MagicPlayTokenAction(
                    event.getPlayer(),
                    TokenCardDefinitions.get("5/5 red Dragon creature token with flying")
                ));
            }
        }
    }
]
