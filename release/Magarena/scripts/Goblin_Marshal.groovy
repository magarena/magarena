def action = {
    final MagicGame game, final MagicEvent event ->
    game.doAction(new MagicPlayTokensAction(
        event.getPlayer(),
        TokenCardDefinitions.get("Goblin1"),
        2
    ));
} as MagicEventAction

def event = {
    final MagicPermanent permanent ->
    return new MagicEvent(
        permanent,
        action,
        "PN puts two 1/1 red Goblin creature tokens onto the battlefield."
    );
}

[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
            return event(permanent);
        }
    },
    new MagicWhenDiesTrigger() {
        @Override
        public MagicEvent getEvent(final MagicPermanent permanent) {
            return event(permanent);
        }
    }
]
