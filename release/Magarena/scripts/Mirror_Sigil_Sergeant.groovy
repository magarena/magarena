[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return (upkeepPlayer.controlsPermanent(MagicColor.Blue)) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(),
                    this,
                    "If PN controls a blue permanent, PN may\$ put a token that's a copy of SN onto the battlefield."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes() && event.getPlayer().controlsPermanent(MagicColor.Blue)) {
                game.doAction(new PlayTokenAction(
                    event.getPlayer(),
                    event.getPermanent()
                ));
            }
        }
    }
]
