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
            final MagicCardList showList = event.getRefPlayer().getHand();
            final MagicCardList choiceList = event.getRefPlayer().getHand();
            for(int i = choiceList.size()-1; i >= 0; i--) {
                if (choiceList.get(i).hasType(MagicType.Land)) {
                    choiceList.remove(i);
                }
            }
            event.isYes() ? game.addEvent(new MagicEvent(
                event.getSource(),
                new MagicFromCardListChoice(choiceList,showList,1,false),
                event.getRefPlayer(),
                Cast,
                ""
            )): game.addEvent(new MagicEvent(
                event.getSource(),
                new MagicFromCardListChoice(choiceList,showList,0,false),
                event.getRefPlayer(),
                Cast,
                ""
            ));
        }
    }
]
