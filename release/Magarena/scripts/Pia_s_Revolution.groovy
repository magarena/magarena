def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.isYes()) {
        game.doAction(new DealDamageAction(event.getSource(), targetPlayer, 3));
    }
    else {
        game.doAction(new ShiftCardAction(event.getRefCard(), MagicLocationType.Graveyard, MagicLocationType.OwnersHand));
    }
}

[
    new OtherPutIntoGraveyardTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MoveCardAction moveAct) {
            final MagicCard sentCard = moveAct.card;
            return (!sentCard.isToken() &&
                sentCard.hasType(MagicType.Artifact) &&
                moveAct.from(MagicLocationType.Battlefield) &&
                moveAct.to(MagicLocationType.Graveyard))
                ?
                new MagicEvent(
                    permanent,
                    TARGET_OPPONENT,
                    sentCard,
                    this,
                    "Return RN to PN's hand unless target opponent\$ has SN deal 3 damage to him or her."
                )
                :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    it,
                    new MagicMayChoice("Has ${event.getSource()} deal 3 damage to you?"),
                    action,
                    "\$"
                );
            });
        }
    }
]

