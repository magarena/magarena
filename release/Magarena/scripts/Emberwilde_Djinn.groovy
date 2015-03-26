[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) == false ?
                new MagicEvent(
                    permanent,
                    upkeepPlayer,
                    new MagicMayChoice(
                        "Pay {R}{R}?",
                        new MagicPayManaCostChoice(MagicManaCost.create("{R}{R}"))
                    ),
                    this,
                    "PN may\$ pay {R}{R}\$ to gain control of SN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new MagicGainControlAction(event.getPlayer(),event.getPermanent()));
            }
        }
    }
]
