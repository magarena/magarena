[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            final MagicPlayer player = permanent.getController();
            return player.getHandSize() > 0 ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(),
                    this,
                    "PN may\$ discard a card at random. " +
                    "If you don't, sacrifice SN."
                ):
                new MagicEvent(
                    permanent,
                    {
                        final MagicGame G, final MagicEvent E ->
                        G.doAction(new MagicSacrificeAction(E.getPermanent()));
                    } as MagicEventAction,
                    "Sacrifice SN."
                );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isNo()) {
                game.doAction(new MagicSacrificeAction(event.getPermanent()));
            } else {
                game.addEvent(MagicDiscardEvent.Random(event.getPermanent(), event.getPlayer(), 1));
            }
        }
    }
]
