def CARD_FROM_LIBRARY = new MagicCardFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
        return true;
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType == MagicTargetType.Library;
    }
};

def A_CARD_FROM_LIBRARY = new MagicTargetChoice(
    CARD_FROM_LIBRARY,
    "a card"
);

def act = {
    final MagicGame game, final MagicEvent event ->
    if (event.isNo() == false) {
        event.processTargetCard(game, {
            final MagicCard card ->
            game.logAppendMessage(event.getPlayer(), "Found " + card + ".");
            game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersLibrary));
            game.doAction(new MagicShuffleLibraryAction(event.getPlayer()));
            game.doAction(new MagicMoveCardAction(card,MagicLocationType.OwnersLibrary,MagicLocationType.TopOfOwnersLibrary));
            game.doAction(new MagicChangeLifeAction(event.getPlayer(),-2));
        });
    }
}

def evt = {
    final MagicEvent event, final MagicChoice choice ->
    return new MagicEvent(
        event.getSource(),
        event.getPlayer(), 
        choice,
        act,
        "PN may search his or her library for a card, shuffle his or her library, and put that card on top of it. PN loses 2 life."
    );
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN may search his or her library for a card, shuffle his or her library, and put that card on top of it. PN loses 2 life."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(evt(
                event,
                A_CARD_FROM_LIBRARY
            ));
        }
    }
]
