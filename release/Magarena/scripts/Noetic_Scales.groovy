[
    new AtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                upkeepPlayer,
                this,
                "Return to its owner's hand each creature PN controls " +
                "with power greater than the number of cards in PN's hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getPlayer().getHandSize();
            final MagicPermanentList all = new MagicPermanentList();
            CREATURE_YOU_CONTROL.filter(event) each {
                if (it.getPower() > amount){
                    all.add(it);
                }
            }
            game.doAction(new RemoveAllFromPlayAction(all, MagicLocationType.OwnersHand));
        }
    }
]
