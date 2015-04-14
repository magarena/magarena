def action = {
    final MagicGame game, final MagicEvent event ->
    event.processTargetCard(game, {
        game.doAction(new RemoveCardAction(it,MagicLocationType.Graveyard));
        game.doAction(new MoveCardAction(it,MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
    });
}

def event = {
    final MagicPermanent permanent ->
    final MagicTargetChoice choice = new MagicTargetChoice(
        CREATURE_CARD_FROM_GRAVEYARD.except(permanent.getCard()),
        MagicTargetHint.None,
        "another target creature card from your graveyard"
    );
    return new MagicEvent(
        permanent,
        choice,
        MagicGraveyardTargetPicker.ReturnToHand,
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
    new MagicWhenSelfLeavesPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicRemoveFromPlayAction act) {
            return event(permanent);
        }
    }
]
