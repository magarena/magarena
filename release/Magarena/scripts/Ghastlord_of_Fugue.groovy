def Exile = {
    final MagicGame game, final MagicEvent event ->
    event.processChosenCards(game, {
        final MagicCard card ->
        game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersHand));
        game.doAction(new MagicMoveCardAction(card,MagicLocationType.OwnersHand,MagicLocationType.Exile));
    });
};

[
    new MagicWhenSelfCombatDamagePlayerTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return new MagicEvent(
                permanent,
                damage.getTarget(),
                this,
                "RN reveals his or her hand. PN chooses a card from it. RN exiles that card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicEvent(
                event.getSource(),
                new MagicFromCardListChoice(event.getRefPlayer().getHand(),1),
                event.getRefPlayer(),
                Exile,
                ""
            ));
        } 
    }
]
