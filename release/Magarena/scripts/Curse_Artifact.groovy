[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.getEnchantedPermanent().getController() == upkeepPlayer ? 
                new MagicEvent(
                    permanent,
                    upkeepPlayer,
                    new MagicMayChoice("Have SN deal 2 damage to you?"),
                    permanent.getEnchantedPermanent(),
                    this,
                    "SN deals 2 damage to RN's controller unless he or she sacrifices RN."
                ):
                MagicEvent.NONE
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent enchanted = event.getRefPermanent();
            if (event.isYes()) {
                game.doAction(new DealDamageAction(event.getPermanent(), event.getRefPlayer(), 2));
            } else {
                game.doAction(new SacrificeAction(enchanted));
            }
        }
    }
]
