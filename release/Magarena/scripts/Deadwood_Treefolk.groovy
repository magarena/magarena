def action = {
    final MagicGame game, final MagicEvent event ->
    event.processTargetCard(game, {
        game.doAction(new ShiftCardAction(
            it,
            MagicLocationType.Graveyard,
            MagicLocationType.OwnersHand
        ));
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
        "PN returns another target creature card from his or her graveyard\$ to his or her hand."
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
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final RemoveFromPlayAction act) {
            return event(permanent);
        }
    }
]
