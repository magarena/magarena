def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.isNo()) {
        game.doAction(new SacrificeAction(event.getPermanent()));
    }
}

[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "PN sacrifices SN unless he or she pays {1} for each card in his or her hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount = player.getHandSize();
            game.addEvent(new MagicEvent(
                event.getSource(),
                player,
                new MagicMayChoice(
                    new MagicPayManaCostChoice(MagicManaCost.create(amount))
                ),
                event.getPermanent(),
                action,
                "PN may\$ pay {"+amount+"}."
            ));
        }
    }
]
