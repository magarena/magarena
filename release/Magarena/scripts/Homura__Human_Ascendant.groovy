[
    new MagicWhenDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent source, final MagicPermanent died) {
            return new MagicEvent(
                source,
                source.getCard(),
                this,
                "Return RN to the battlefield flipped."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCard card = event.getRefCard();
            if (card.isInGraveyard()) {
                game.doAction(new RemoveCardAction(card,MagicLocationType.Graveyard));
                game.doAction(new PlayCardAction(card,event.getPlayer(),MagicPlayMod.FLIPPED));
            }
        }
    }
]
