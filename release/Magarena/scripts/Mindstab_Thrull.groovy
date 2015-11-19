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
                    "If PN does, defending player discards 3 cards."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent it = event.getPermanent();
            if (event.isYes() && it.isValid()) {
                game.doAction(new SacrificeAction(it));
                game.addEvent(new MagicDiscardEvent(event.getSource(), game.getDefendingPlayer(), 3));
            }
        }
    }
]
