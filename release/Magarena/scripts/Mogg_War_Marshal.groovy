def action = {
    final MagicGame game, final MagicEvent event ->
    game.doAction(new MagicPlayTokenAction(
        event.getPlayer(),
        TokenCardDefinitions.get("Goblin1")
    ));
} as MagicEventAction

def event = {
    final MagicPermanent permanent ->
    return new MagicEvent(
        permanent,
        action,
        "PN puts a 1/1 red Goblin creature token onto the battlefield."
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
