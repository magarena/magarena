def choice = new MagicTargetChoice("an enchantment card from your library");
[
    new MagicWhenDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
            return permanent.getCounters(MagicCounterType.Time) == 0 ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(),
                    this,
                    "PN may\$ search your library for an enchantment card and put it onto the battlefield. " +
                    "If you do, shuffle your library."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.addEvent(new MagicSearchOntoBattlefieldEvent(
                    event.getSource(),
                    event.getPlayer(),
                    choice
                ));
            }
        }
    }
]
