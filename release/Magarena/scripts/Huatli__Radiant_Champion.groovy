[
    new MagicPlaneswalkerActivation(1) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Put a loyalty counter on SN for each creature PN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getPlayer().getNrOfPermanents(MagicType.Creature);
            game.doAction(new ChangeCountersAction(event.getPlayer(), event.getPermanent(), MagicCounterType.Loyalty, amount));
        }
    }
]

