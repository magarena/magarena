[
    new MagicWhenBecomesUntappedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent untapped) {
            return (permanent == untapped) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        new MagicPayManaCostChoice(MagicManaCost.create("{2}{U}")),
                    ),
                    this,
                    "PN may\$ pay {2}{U}\$. If PN does, " +
                    "put a 2/2 blue Bird enchantment creature token with flying onto the battlefield."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new MagicPlayTokenAction(event.getPlayer(), TokenCardDefinitions.get("2/2 blue Bird enchantment creature token with flying")));
            }
        }
    }
]
