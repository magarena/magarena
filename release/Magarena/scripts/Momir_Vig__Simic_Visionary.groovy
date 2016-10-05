[
    new OtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return permanent.isFriend(cardOnStack) &&
                   cardOnStack.hasType(MagicType.Creature) &&
                   cardOnStack.hasColor(MagicColor.Blue) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN reveals the top card of his or her library. "+
                    "If it's a creature card, PN puts that card into his or her hand."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicCard card : event.getPlayer().getLibrary().getCardsFromTop(1)) {
                game.doAction(new RevealAction(card));
                if (card.hasType(MagicType.Creature)) {
                    game.doAction(new ShiftCardAction(
                        card,
                        MagicLocationType.OwnersLibrary,
                        MagicLocationType.OwnersHand
                    ));
                }
            }
        }
    }
]
