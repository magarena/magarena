def INSTANT_OR_SORCERY_CARD_FROM_LIBRARY = new MagicCardFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
        return target.hasType(MagicType.Instant) || target.hasType(MagicType.Sorcery);
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType == MagicTargetType.Library;
    }
};

def AN_INSTANT_OR_SORCERY_CARD_FROM_LIBRARY = new MagicTargetChoice(
    INSTANT_OR_SORCERY_CARD_FROM_LIBRARY,
    "an instant or sorcery card"
);

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
        "PN may search his or her library for an instant or sorcery card, reveals it, shuffle his or her library, and put that card on top of it."
    );
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN may search his or her library for an instant or sorcery card, reveals it, shuffle his or her library, and put that card on top of it."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(evt(
                event,
                AN_INSTANT_OR_SORCERY_CARD_FROM_LIBRARY
            ));
        }
    }
]