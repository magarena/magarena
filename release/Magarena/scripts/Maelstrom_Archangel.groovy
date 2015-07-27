def NONLAND_CARD_FROM_HAND = new MagicCardFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
        return !(target.hasType(MagicType.Land));
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType == MagicTargetType.Hand;
    }
}; 
def A_NONLAND_CARD_FROM_HAND = new MagicTargetChoice(
    NONLAND_CARD_FROM_HAND,  
    MagicTargetHint.None,
    "a nonland card from your hand"
);
[
    new MagicWhenSelfCombatDamagePlayerTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    A_NONLAND_CARD_FROM_HAND
                ),
                MagicGraveyardTargetPicker.PutOntoBattlefield,
                this,
                "PN may\$ cast target nonland card\$ from his or her hand without paying its mana cost."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetCard(game, {
                    final MagicPlayer player = event.getPlayer();
                    for (final MagicEvent cevent : it.getAdditionalCostEvent()) {
                        if (cevent.isSatisfied() == false) {
                            game.logAppendMessage(player, "Casting failed as " + player + " is unable to pay additional casting costs.");
                            return;
                        }
                    }
                    for (final MagicEvent cevent : it.getAdditionalCostEvent()) {
                        game.addEvent(cevent);
                    }
                    game.doAction(new RemoveCardAction(it, MagicLocationType.OwnersHand));
                    game.addEvent(new MagicPutCardOnStackEvent(
                        it, 
                        player, 
                        MagicLocationType.OwnersHand, 
                        MagicLocationType.Graveyard
                    ));
                });
            }
        }
    }
]
