[
    new OtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return cardOnStack.hasType(MagicType.Instant) ?
                new MagicEvent(
                    permanent,
                    cardOnStack,
                    this,
                    "Counter RN unless its controller pays {X}, where X is RN's converted mana cost."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getRefCardOnStack().getConvertedCost();
            game.addEvent(new MagicCounterUnlessEvent(
                event.getSource(),
                event.getRefCardOnStack(),
                MagicManaCost.create(amount)
            ));
        }
    }
]
