[
    new ThisBecomesTappedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent tapped) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    new MagicPayManaCostChoice(MagicManaCost.create("{X}"))
                ),
                this,
                "PN may\$ pay {X}\$. If PN does, reveal the top X cards of his or her library, "+
                "put all Dwarf and Vehicle cards from among them into his or her hand, then put the rest on the bottom of his or her library in a random order."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
				final MagicPlayer player = event.getPlayer();
				final MagicCardList cards = player.getLibrary().getCardsFromTop(event.getPaidMana().getX());
				game.doAction(new RevealAction(cards));
				for (final MagicCard card : cards) {
					if (card.hasSubType(MagicSubType.Dwarf) || card.hasSubType(MagicSubType.Vehicle))  {
						game.doAction(new ShiftCardAction(card, MagicLocationType.OwnersLibrary, MagicLocationType.OwnersHand));
					} else {
						game.doAction(new ShiftCardAction(card, MagicLocationType.OwnersLibrary, MagicLocationType.BottomOfOwnersLibrary));                   
					}
				}
			}
        }
    }
]
