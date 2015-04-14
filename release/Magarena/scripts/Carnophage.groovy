[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Pay 1 life?"),
                this,
                "PN may\$ pay 1 life. If PN doesn't, tap SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.addEvent(new MagicPayLifeEvent(event.getSource(), event.getPlayer(), 1));
            } else {
                game.doAction(new TapAction(event.getPermanent()));
            }
        }
    }
]
