def act = {
    final MagicGame game, final MagicEvent event ->
    if (event.isNo() == false) {
        event.processTargetCard(game, new MagicCardAction() {
            public void doAction(final MagicCard card) {
                game.logAppendMessage(event.getPlayer(), "Found " + card + ".");
                game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersLibrary));
                game.doAction(new MagicShuffleLibraryAction(event.getPlayer()));
                game.doAction(new MagicMoveCardAction(card,MagicLocationType.OwnersLibrary,MagicLocationType.OwnersHand));
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
        "PN may search PN's library for an Aurochs card, reveal it, and put it into PN's hand."
    );
}
[
    new MagicWhenComesIntoPlayTrigger() {
       @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {      
            return new MagicEvent(
                permanent,
                this,
                "PN may search PN's library for an Aurochs card, reveal it, and put it into PN's hand. If PN does, shuffle PN's library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(evt(
                event,
                new MagicMayChoice(
                    "Search for an Aurochs card?",
                    new MagicTargetChoice("an Aurochs card from your library")
                )
            ));
        }
    }
]
