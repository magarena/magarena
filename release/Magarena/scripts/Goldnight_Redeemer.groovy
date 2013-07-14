[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "PN gains 2 life for each other creature he or she controls."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int x = player.controlsPermanent(event.getPermanent()) ? 1 : 0;
            final int amount = player.getNrOfPermanents(MagicType.Creature) - x;
            if (amount > 0) {
                game.doAction(new MagicChangeLifeAction(player,amount * 2));
            }
        }
    }
]
