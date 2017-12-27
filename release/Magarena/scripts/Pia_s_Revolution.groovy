def sentCard = MagicCard.NONE

def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.isYes()) {
        game.doAction(new DealDamageAction(event.getSource(), targetPlayer, 3));
    }
    else {
        game.doAction(new MoveCardAction(sentCard, event.getSource(), MagicLocationType.Graveyard, MagicLocationType.OwnersHand));
    }
}

[
    new OtherPutIntoGraveyardTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MoveCardAction moveAct) {
            sentCard = moveAct.card;
            if (!sentCard.isToken() &&
                sentCard.hasType(MagicType.Artifact) &&
                moveAct.from(MagicLocationType.Battlefield) &&
                moveAct.to(MagicLocationType.Graveyard)) {
                return new MagicEvent(
                    permanent,
                    TARGET_OPPONENT,
                    this,
                    "Whenever a nontoken artifact is put into PN's graveyard from the battlefield, " +
                    "return that card to PN's hand unless target opponent\$ has SN deal 3 damage to him or her."
                );
            }
            else {
                return MagicEvent.NONE;
            }
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicPlayer targetPlayer ->
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    targetPlayer,
                    new MagicMayChoice("Has Pia's Revolution deal 3 damage to you?"),
                    action,
                    "\$"
                );
            });
        }
    }
]

