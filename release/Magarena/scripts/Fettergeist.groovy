def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.isNo()) {
        game.doAction(new MagicSacrificeAction(event.getPermanent()));
    }
} as MagicEventAction ;

[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Sacrifice SN unless you pay " +
                    "{1} for each other creature you control."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int x = event.getPlayer().controlsPermanent(event.getPermanent()) ? 1 : 0;
            final int amount = event.getPlayer().getNrOfPermanents(MagicType.Creature) - x;
            final MagicManaCost cost = MagicManaCost.create("{"+amount+"}");
            final MagicEvent triggerEvent = new MagicEvent(
                event.getSource(),
                event.getPlayer(),
                new MagicMayChoice(
                    new MagicPayManaCostChoice(cost)
                ),
                action,
                "You may\$ pay " + cost.getText() + "\$. If you don't, sacrifice SN."
            );
            game.doAction(new MagicPutItemOnStackAction(new MagicTriggerOnStack(triggerEvent)));
        }
    }
]
