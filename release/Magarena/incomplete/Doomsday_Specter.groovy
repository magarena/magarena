def Discard = {
    final MagicGame game, final MagicEvent event ->
    event.processChosenCards(game, {
        final MagicCard card ->
        game.doAction(new MagicDiscardCardAction(event.getRefPlayer(),card));
    });
};


[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.isSource(permanent) && damage.isCombat() && damage.isTargetPlayer()) ?
                new MagicEvent(
                    permanent,
                    damage.getTarget(),
                    this,
                    "PN looks at RN's hand and chooses a card from it. RN discards that card."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicEvent(
                event.getSource(),
                new MagicFromCardListChoice(event.getRefPlayer().getHand(),1),
                event.getRefPlayer(),
                Discard,
                ""
            ));
        }
    }
]
