[
    new OtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return (cardOnStack.hasType(MagicType.Creature)) ?
                MagicEvent.NONE:
                new MagicEvent(
                    permanent,
                    cardOnStack,
                    this,
                    "Counter RN. ${cardOnStack.getController()} creates X 1/1 white and blue Bird creature tokens with flying, " +
                    "where X is RN's converted mana cost. (${cardOnStack.getConvertedCost()})"
                );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PlayTokensAction(
                event.getRefCardOnStack().getController(),
                CardDefinitions.getToken("1/1 white and blue Bird creature token with flying"),
                event.getRefCardOnStack().getConvertedCost()
            ));
            game.doAction(new CounterItemOnStackAction(event.getRefCardOnStack()));
        }
    }
]
