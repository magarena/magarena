def Cast = {
    final MagicGame game, final MagicEvent event ->
    if (event.getCardChoice().size() > 0) {
        final MagicCard card = (MagicCard)event.getCardChoice().get(0);
        game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersHand));
        final MagicCardOnStack cardOnStack=new MagicCardOnStack(card,event.getPlayer(), MagicPayedCost.NO_COST);
        game.doAction(new MagicPutItemOnStackAction(cardOnStack));
    }
};

[
new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.getSource() == permanent &&
                    permanent.isOpponent(damage.getTarget()) &&
                    damage.isCombat()) ?
                new MagicEvent(
                    permanent,
                    permanent.getController(),
                    new MagicMayChoice(),
                    (MagicPlayer) damage.getTarget(),
                    this,
                    "RN reveals his or her hand. PN may cast a nonland card from it."
                ):
                MagicEvent.NONE;
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
