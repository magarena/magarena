[
    new MagicWhenBecomesUntappedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent untapped) {
            return (permanent == untapped) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        new MagicPayManaCostChoice(MagicManaCost.create("{2}{W}")),
                    ),
                    this,
                    "PN may\$ pay {2}{W}\$. If PN does, " +
                    "put two 1/1 white Soldier enchantment creature token onto the battlefield."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new MagicPlayTokensAction(event.getPlayer(), TokenCardDefinitions.get("1/1 white Soldier enchantment creature token"),2));
            }
        }
    }
]
