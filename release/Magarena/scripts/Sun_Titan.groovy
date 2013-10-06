def action = {
    final MagicGame game, final MagicEvent event ->
    event.processTargetCard(game,new MagicCardAction() {
            public void doAction(final MagicCard card) {
            game.doAction(new MagicReanimateAction(
                card,
                event.getPlayer()
            ));
        }
    });
} as MagicEventAction

def event = {
    final MagicPermanent permanent ->
    return new MagicEvent(
        permanent,
        MagicTargetChoice.TARGET_PERMANENT_CARD_CMC_LEQ_3_FROM_GRAVEYARD,
        MagicGraveyardTargetPicker.PutOntoBattlefield,
        action,
        "Return target permanent card\$ with converted mana cost 3 or less " +
        "from your graveyard to the battlefield."
    );
}

[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return event(permanent);
        }
    },
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
            return (permanent==attacker) ? event(permanent) : MagicEvent.NONE;
        }
    }
]
