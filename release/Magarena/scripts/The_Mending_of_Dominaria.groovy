def returnAction = {
    final MagicGame game, final MagicEvent event ->
    if (event.isYes()) {
        event.processTargetCard(game, {
            game.doAction(new ShiftCardAction(it, MagicLocationType.Graveyard, MagicLocationType.OwnersHand));
        });
    }
}

def chapter12Action = {
    final MagicGame game, final MagicEvent event ->
    game.doAction(new MillLibraryAction(event.getPlayer(), 2));
    game.addEvent(new MagicEvent(
        event.getSource(),
        new MagicMayChoice(new MagicTargetChoice("a creature card from your graveyard")),
        returnAction,
        "then PN may\$ return a creature card from PN's graveyard\$ to PN's hand."
    ));
}

def chapter12event = {
    final MagicPermanent permanent ->
    return new MagicEvent(
        permanent,
        chapter12Action,
        "Put the top two cards of PN's library into PN's graveyard,"
    );
}

[
    new SagaChapterTrigger(1) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicCounterChangeTriggerData data) {
            return chapter12event(permanent);
        }
    },
    new SagaChapterTrigger(2) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicCounterChangeTriggerData data) {
            return chapter12event(permanent);
        }
    },
    new SagaChapterTrigger(3) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicCounterChangeTriggerData data) {
            return new MagicEvent(
                permanent,
                this,
                "Return all land cards from PN's graveyard to the battlefield, " +
                "then shuffle PN's graveyard into PN's library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            card(MagicType.Land).from(MagicTargetType.Graveyard).filter(event) each {
                game.doAction(new ReturnCardAction(MagicLocationType.Graveyard, it, event.getPlayer()));
            }
            game.doAction(new ShuffleCardsIntoLibraryAction(event.getPlayer().getGraveyard(), MagicLocationType.Graveyard));
        }
    }
]

