def TARGET_NONLAND_CARD_FROM_HAND = new MagicCardFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
        return !(target.hasType(MagicType.Land));
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType == MagicTargetType.Hand;
    }
}; 
def NONLAND_CARD_FROM_HAND = new MagicTargetChoice(
    TARGET_NONLAND_CARD_FROM_HAND,  
    MagicTargetHint.None,
    "a nonland card from your hand"
);
[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.getSource()==permanent &&
                    damage.getTarget().isPlayer() &&
                    damage.isCombat()) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        NONLAND_CARD_FROM_HAND
                    ),
                    MagicGraveyardTargetPicker.PutOntoBattlefield,
                    this,
                    "PN may\$ cast target nonland card\$ from his or her hand without paying its mana cost."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetCard(game, {
                    final MagicCard card ->
                    game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersHand));
                    final MagicCardOnStack cardOnStack=new MagicCardOnStack(card,event.getPlayer(), MagicPayedCost.NO_COST);
                    game.doAction(new MagicPutItemOnStackAction(cardOnStack));
                });
            }
        }
    }
]
