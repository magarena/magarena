[
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return new MagicEvent(
                permanent,
				cardOnStack,
                this,
                "Counter RN unless "+cardOnStack.getController().toString()+" pays {X}, where X is the number of cards in all graveyards with the same name as RN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
			final MagicCardOnStack card = event.getRefCardOnStack();
			final MagicPlayer player = card.getController();
			final String name = card.getCard().getName();
			final int graveyard = game.filterCards(player,MagicTargetFilterFactory.cardName(name).from(MagicTargetType.Graveyard)).size();
			final int oppGraveyard = game.filterCards(player,MagicTargetFilterFactory.cardName(name).from(MagicTargetType.OpponentsGraveyard)).size();
			final int amount = graveyard + oppGraveyard;
			game.logAppendMessage(event.getPermanent().getController(),"(X="+amount+")")
			if (amount > 0) {
				game.addEvent(new MagicCounterUnlessEvent(event.getSource(),card,MagicManaCost.create("{"+amount+"}")));
			}
        }
    }
]
