def mainEvent = {
    final MagicPermanent permanent, final MagicEventAction action ->
    return new MagicEvent(
        permanent,
        action,
        "Each player returns two land cards from their graveyard to the battlefield."
    );
}

def putlands = {
    final MagicGame game, final MagicEvent event ->
    event.processChosenCards(game, {
        game.doAction(new ReturnCardAction(
            MagicLocationType.Graveyard,
            it,
            event.getPlayer()
        ));
    });
}

def act = {
    final MagicGame game, final MagicEvent event ->
    for (final MagicPlayer player : game.getAPNAP()) {
        final MagicCardList graveyard = player.getGraveyard();
        game.addEvent(new MagicEvent(
            event.getSource(),
            player,
            new MagicFromCardListChoice(
                graveyard.findAll({ it.hasType(MagicType.Land) }),
                graveyard,
                2
            ),
            putlands,
            "PN returns \$."
        ));
    }
}

[
    new SagaChapterTrigger(2) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicCounterChangeTriggerData data) {
            return mainEvent(permanent, act);
        }
    },


    new SagaChapterTrigger(3) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicCounterChangeTriggerData data) {
            return mainEvent(permanent, act);
        }
    }
]
