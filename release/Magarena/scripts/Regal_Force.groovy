[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPlayer player) {
            return new MagicEvent(
                permanent,
                this,
                "PN draws a card for each green creature he or she controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final Collection<MagicPermanent> targets = game.filterPermanents(player,MagicTargetFilter.TARGET_GREEN_CREATURE_YOU_CONTROL);
            game.doAction(new MagicDrawAction(
                player,
                targets.size()
            ));

        }
    }
]
