[
    new MagicWhenAttacksUnblockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (creature == permanent) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(),
	            this,
                    "PN may\$ sacrifice SN. " +
                    "If you do, defending player discards 3 cards."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new MagicSacrificeAction(event.getPermanent()));
                game.addEvent(new MagicDiscardEvent(
                event.getPermanent(),
                event.getPermanent().getOpponent(),
		3
            ));
            }
        }
    }
]
