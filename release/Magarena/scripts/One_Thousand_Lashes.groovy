[
    new AtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            final MagicPermanent enchantedCreature = permanent.getEnchantedPermanent();
            return (enchantedCreature.isCreature() && enchantedCreature.isController(upkeepPlayer)) ?
                new MagicEvent(
                    permanent,
                    upkeepPlayer,
                    this,
                    "PN loses 1 life."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeLifeAction(event.getPlayer(), -1));
        }
    }
]
