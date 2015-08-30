[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "PN draws a card if he or she " +
                "controls the creature with the greatest " +
                "power or tied for the greatest power."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final Collection<MagicPermanent> targets = CREATURE.filter(event);
            MagicPermanent highest = MagicPermanent.NONE;
            for (final MagicPermanent creature : targets) {
                if (creature.getPower() > highest.getPower()) {
                    highest = creature;
                }
            }
            if (highest.isController(player)) {
                game.doAction(new DrawAction(player));
            }
        }
    }
]
