[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return (permanent.isController(upkeepPlayer)&&
                    permanent.isTapped()) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        new MagicPayManaCostChoice(MagicManaCost.create("{4}"))
                    ),
                    this,
                    "PN may\$ pay {4}. If you do, untap SN."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new MagicUntapAction(event.getPermanent()));
            }
        }
    }
]
