def act = {
    final MagicGame game, final MagicEvent event ->
    if (event.isNo() == false) {
        event.processTargetCard(game, new MagicCardAction() {
            public void doAction(final MagicCard card) {
                game.logAppendMessage(event.getPlayer(), "Found " + card + ".");
                game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersLibrary));
                game.doAction(new MagicShuffleLibraryAction(event.getPlayer()));
                game.doAction(new MagicMoveCardAction(card,MagicLocationType.OwnersLibrary,MagicLocationType.TopOfOwnersLibrary));
            }
        });
    }
} as MagicEventAction

def evt = {
    final MagicEvent event, final MagicChoice choice ->
    return new MagicEvent(
        event.getSource(),
        event.getPlayer(), 
        choice,
        act,
        "PN may search his or her library for a Kithkin card, reveal it, shuffle his or her library, and put that card on top of it."
    );
}
[
    new MagicWhenComesIntoPlayTrigger() {
       @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {      
            return new MagicEvent(
                permanent,
                this,
                "PN may search his or her library for a Kithkin card, reveal it, put it into his or her hand, and shuffle his or her library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(evt(
                event,
                new MagicMayChoice(
                    "Search for a Kithkin card?",
                    new MagicTargetChoice("a Kithkin card from your library")
                )
            ));
        }
    }
]
