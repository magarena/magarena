[
    new MagicAtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer eotPlayer) {
            final MagicPermanent enchanted = permanent.getEnchantedPermanent();
            final int cost = enchanted.getConvertedCost();
            return enchanted.isController(eotPlayer) ?
                new MagicEvent(
                    permanent,
                    eotPlayer,
                    enchanted,
                    this,
                    "PN sacrifices RN."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new SacrificeAction(event.getRefPermanent()));
        }
    }
]
