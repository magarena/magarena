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
    final MagicTargetChoice choice = new MagicTargetChoice(
        new MagicTargetFilter.MagicOtherCardTargetFilter(
            MagicTargetFilter.TARGET_CREATURE_CARD_FROM_GRAVEYARD,
            permanent.getCard()
        ),
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
    new MagicWhenLeavesPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicRemoveFromPlayAction act) {
            return act.isPermanent(permanent) ? event(permanent) : MagicEvent.NONE;
        }
    }
]
