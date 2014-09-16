[
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return (cardOnStack.hasType(MagicType.Instant)) ?
                new MagicEvent(
                    permanent,
                cardOnStack,
                    this,
                    "Whenever a player casts an instant spell, counter it unless that player pays {X}, where X is its converted mana cost."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
                final int amount = event.getRefCardOnStack().getConvertedCost();
                game.addEvent(new MagicCounterUnlessEvent(event.getSource(),event.getRefCardOnStack(),MagicManaCost.create("{"+amount+"}")));
        }
    }
]
