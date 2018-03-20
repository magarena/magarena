[
    new ThisDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
            return (died == permanent) ?
                new MagicEvent(
                permanent,
                new MagicMayChoice(
                    new MagicPayManaCostChoice(MagicManaCost.create("{1}{B}"))
                ),
                died.getCard(),
                this,
                "PN may\$ pay {1}{B}\$. If PN does, return SN to the battlefield tapped under his or her control."
            ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new ReanimateAction(event.getRefCard(), event.getPlayer(), MagicPlayMod.TAPPED));
            }
        }
    }
]
