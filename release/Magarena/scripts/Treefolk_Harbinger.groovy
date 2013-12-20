def TREEFOLK_OR_FOREST_CARD_FROM_LIBRARY = new MagicCardFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
        return target.hasSubType(MagicSubType.Forest) || target.hasSubType(MagicSubType.Treefolk);
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType==MagicTargetType.Library;
    }
};

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
        "PN may search his or her library for a Treefolk or Forest card, reveal it, shuffle his or her library, and put that card on top of it."
    );
}
[
    new MagicWhenComesIntoPlayTrigger() {
       @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {      
            return new MagicEvent(
                permanent,
                this,
                "PN may search his or her library for a Treefolk or Forest card, reveal it, put it into his or her hand, and shuffle his or her library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(evt(
                event,
                new MagicMayChoice(
                    "Search for a Treefolk or Forest card?",
                    new MagicTargetChoice(TREEFOLK_OR_FOREST_CARD_FROM_LIBRARY, "a Treefolk or Forest card from your library")
                )
            ));
        }
    }
]