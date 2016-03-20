[
    new AtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer eotPlayer) {
            return permanent.isController(eotPlayer) ? 
                new MagicEvent(
                    permanent,
                    this,
                    "PN Bolsters X, where X is the number of tapped creatures PN controls."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount = player.getNrOfPermanents(TAPPED_CREATURE_YOU_CONTROL);
            game.logAppendValue(player,amount); 
            final Collection<MagicPermanent> targets = MagicTargetFilterFactory.CREATURE_YOU_CONTROL.filter(event);
            int minToughness = Integer.MAX_VALUE;
            for (final MagicPermanent creature : targets) {
                minToughness = Math.min(minToughness, creature.getToughnessValue());
            }
            game.addEvent(new MagicBolsterEvent(event.getSource(), player, amount, minToughness));
        }
    }
]
