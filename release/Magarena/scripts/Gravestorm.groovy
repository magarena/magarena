def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.isYes()) {
    event.processTargetCard(game, {
        game.doAction(new ShiftCardAction(it,MagicLocationType.Graveyard,MagicLocationType.Exile));
    });
    } else {
        game.addEvent(MagicRuleEventAction.create("You may draw a card.").getEvent(event.getSource()));
    }
}

def choice = new MagicTargetChoice("a card from your graveyard");

[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                permanent.getController(),
                TARGET_OPPONENT,
                this,
                "Target opponent\$ may\$ exile a card from his or her graveyard. "+
                "If he or she doesn't, PN may\$ draw a card"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    it,
                    new MagicMayChoice(choice),
                    MagicGraveyardTargetPicker.ExileOwn,
                    action,
                    "PN exiles a card\$ from his or her graveyard."
                ));
            });
        }
    }
]
