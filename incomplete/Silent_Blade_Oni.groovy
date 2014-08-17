def Cast = {
    final MagicGame game, final MagicEvent event ->
    event.processChosenCards(game, {
        final MagicCard card ->
        game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersHand));
        final MagicCardOnStack cardOnStack=new MagicCardOnStack(card,event.getPlayer(), MagicPayedCost.NO_COST);
        game.doAction(new MagicPutItemOnStackAction(cardOnStack));
    });
};

[
    new MagicWhenSelfCombatDamagePlayerTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(),
                damage.getTarget(),
                this,
                "RN reveals his or her hand. PN may cast a nonland card from it."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final List<MagicCard> showList = event.getRefPlayer().getHand();
            if (event.isYes()) {
                final MagicCardList choiceList = new MagicCardList(showList);
                for(int i = choiceList.size()-1; i >= 0; i--) {
                    if (choiceList.get(i).hasType(MagicType.Land)) {
                        choiceList.remove(i);
                    }
                }
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    new MagicFromCardListChoice(choiceList,showList,1,false),
                    event.getRefPlayer(),
                    Cast,
                    ""
                ));
            } else {
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    new MagicFromCardListChoice(showList),
                    event.getRefPlayer(),
                    Cast,
                    ""
                ));
            }
        }
    }
]
