[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "PN draws a card if he or she controls the creature with the greatest toughness or tied for the greatest toughness."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            MagicPermanent highest = MagicPermanent.NONE;
            CREATURE.filter(event) each {
                if (it.getToughness() > highest.getToughness()) {
                    highest = it;
                }
            }
            if (highest.isController(player)) {
                game.doAction(new DrawAction(player));
            }
        }
    }
]
