def action = {
    final MagicGame game, final MagicEvent event ->
    event.processTargetCard(game,new MagicCardAction() {
        public void doAction(final MagicCard targetCard) {
            game.doAction(new MagicRemoveCardAction(targetCard,MagicLocationType.Graveyard));
            game.doAction(new MagicMoveCardAction(targetCard,MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
        }
    });
} as MagicEventAction

def event = {
    final MagicPermanent permanent ->
    return new MagicEvent(
        permanent,
        MagicTargetChoice.TARGET_CREATURE_CARD_FROM_GRAVEYARD,
        new MagicGraveyardTargetPicker(false),
        action,
        "Return another target creature card\$ from your graveyard to your hand."
    );
}

[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return event(permanent);
        }
    },
    new MagicWhenLeavesPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent left) {
            return (permanent == left) ? event(permanent) : MagicEvent.NONE; 
        }
    }
]
