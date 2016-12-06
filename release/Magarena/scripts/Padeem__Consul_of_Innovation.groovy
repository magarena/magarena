[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "PN draws a card if he or she " +
                "controls the artifact with the highest " +
                "converted mana cost or tied for the highest converted mana cost."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final Collection<MagicPermanent> targets = ARTIFACT.filter(event);
            MagicPermanent highest = MagicPermanent.NONE;
            for (final MagicPermanent artifact : targets) {
                if (artifact.getConvertedCost() > highest.getConvertedCost()) {
                    highest = artifact;
                }
                if (artifact.getConvertedCost() == highest.getConvertedCost() && artifact.isController(player)) {
                    highest = artifact;
                }
            }
            if (highest.isController(player)) {
                game.doAction(new DrawAction(player));
            }
        }
    }
]
