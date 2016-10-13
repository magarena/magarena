def DelayedTrigger = {
    final MagicSource staleSource, final MagicPlayer stalePlayer ->
    return new AtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer eotPlayer) {
            game.addDelayedAction(new RemoveTriggerAction(this));
            return new MagicEvent(
                game.createDelayedSource(staleSource, stalePlayer),
                this,
                "PN creates a 4/4 red Bird creature token with flying."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PlayTokenAction(
                event.getPlayer(),
                CardDefinitions.getToken("4/4 red Bird creature token with flying")
            ))
        }
    }
}

[
    new ThisDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
            return new MagicEvent(
                permanent,
                this,
                "PN creates a 4/4 red Bird creature token with flying at the beginning of the next end step."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new AddTriggerAction(
                DelayedTrigger(event.getSource(), event.getPlayer())
            ));
        }
    }
]
