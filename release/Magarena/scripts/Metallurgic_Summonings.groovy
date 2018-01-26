[
    new OtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicCardOnStack cardOnStack) {
            final MagicCard card = cardOnStack.getCard();
            return ((permanent.isController(card.getController())) &&
                (card.hasType(MagicType.Instant) || card.hasType(MagicType.Sorcery)))
                ?
                new MagicEvent(
                    cardOnStack,
                    cardOnStack,
                    this,
                    "PN creates an X/X colorless Construct artifact creature token, where X is RN's converted mana cost."
                )
                :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int x = event.getRefCardOnStack().getConvertedCost();
            game.doAction(new PlayTokenAction(
                event.getPlayer(),
                CardDefinitions.getToken(x, x, "colorless Construct artifact creature token")
            ));
        }
    }
]
