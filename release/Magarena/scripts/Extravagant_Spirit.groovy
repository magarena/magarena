def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.isNo()) {
        game.doAction(new MagicSacrificeAction(event.getPermanent()));
    }
} ;

[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN sacrifices SN unless PN pays " +
                    "{1} for each card in his or her hand."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getPlayer().getHandSize();
            final MagicManaCost cost = MagicManaCost.create("{"+amount+"}");
            final MagicEvent triggerEvent = new MagicEvent(
                event.getSource(),
                event.getPlayer(),
                new MagicMayChoice(
                    new MagicPayManaCostChoice(cost)
                ),
                action,
                "PN may\$ pay " + cost.getText() + "\$. If PN doesn't, sacrifice SN."
            );
            game.doAction(new MagicPutItemOnStackAction(new MagicTriggerOnStack(triggerEvent)));
        }
    }
]
