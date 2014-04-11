[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicTarget target = damage.getTarget();
            if (damage.getSource() == permanent &&
                   damage.isCombat() &&
                   target.isPlayer()) {
                final MagicPlayer player = (MagicPlayer) target;
                final MagicCardList showList = player.getHand();
                final MagicCardList choiceList = player.getHand();
                for(int i = choiceList.size()-1; i >= 0; i--) {
                    if (choiceList.get(i).hasType(MagicType.Land)) {
                        choiceList.remove(i);
                    }
                }
                return new MagicEvent(
                    permanent,
                    new MagicFromCardListChoice(choiceList,showList,1,false),
                    damage.getTarget(),
                    this,
                    "RN reveals his or her hand. PN casts a nonland card from it."
                )

            } else return MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getCardChoice().size() > 0) {
                final MagicCard card = (MagicCard)event.getCardChoice().get(0);
                game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersHand));
                final MagicCardOnStack cardOnStack=new MagicCardOnStack(card,event.getPlayer(), MagicPayedCost.NO_COST);
                game.doAction(new MagicPutItemOnStackAction(cardOnStack));
            }
        }
    }
]
