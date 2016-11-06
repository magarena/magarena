[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Destroy all creatures, PN then creates an X/X colorless Horror artifact creature token, "+
                "where X is the number of creatures destroyed this way."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final Collection<MagicPermanent> targets = CREATURE.filter(event);
            final DestroyAction destroy = new DestroyAction(targets);
            game.doAction(destroy);
            final int amount = destroy.getNumDestroyed();
            game.logAppendX(player, amount);
            game.doAction(new PlayTokenAction(
                player,
                CardDefinitions.getToken(amount, amount, "colorless Horror artifact creature token")
            ));
        }
    }
]
