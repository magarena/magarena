[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            final MagicPermanent enchanted = permanent.getEnchantedPermanent();
            return enchanted.isController(upkeepPlayer) && enchanted.isValid()?
                new MagicEvent(
                    permanent,
                    upkeepPlayer,
                    new MagicMayChoice(),
                    enchanted,
                    this,
                    "PN may\$ sacrifice RN. If he or she doesn't, SN deals 2 damage to PN."
                ):
                MagicEvent.NONE
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent enchanted = event.getRefPermanent();
            final MagicPlayer player = event.getPlayer();
            if (event.isYes() && enchanted.isController(player)) {
                game.doAction(new SacrificeAction(enchanted));
            } else {
                game.doAction(new DealDamageAction(event.getPermanent(), event.getPlayer(), 2));
            }
        }
    }
]
