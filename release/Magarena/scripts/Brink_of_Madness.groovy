[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return permanent.getController().getHandSize() == 0 ?
                new MagicEvent(
                    permanent,
                    TARGET_OPPONENT,
                    this,
                    "PN sacrifices SN and target opponent\$ discards his or her hand."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.doAction(new SacrificeAction(event.getPermanent()));
                game.addEvent(new MagicDiscardHandEvent(event.getSource(), it));
            });
        }
    }
]
