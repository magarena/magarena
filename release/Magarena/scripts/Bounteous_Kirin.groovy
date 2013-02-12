[
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicCardOnStack cardOnStack) {
            return permanent.isFriend(cardOnStack) && 
                   (cardOnStack.hasSubType(MagicSubType.Spirit) || cardOnStack.hasSubType(MagicSubType.Arcane)) ?
                new MagicEvent(
                    permanent,
                    new MagicSimpleMayChoice(
                        MagicSimpleMayChoice.GAIN_LIFE,
                        cardOnStack.getConvertedCost(),
                        MagicSimpleMayChoice.DEFAULT_YES
                    ),
                    cardOnStack,
                    this,
                    "You may gain " + cardOnStack.getConvertedCost() + " life."
                ):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                final MagicCardOnStack spell = event.getRefCardOnStack();
                game.doAction(new MagicChangeLifeAction(event.getPlayer(), spell.getConvertedCost()));
            }
        }
    }
]
