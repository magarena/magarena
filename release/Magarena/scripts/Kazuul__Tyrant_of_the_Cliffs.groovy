def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.isYes()) {
        event.payManaCost(game);
    } else {
        game.doAction(new MagicPlayTokenAction(event.getRefPlayer(),TokenCardDefinitions.get("3/3 red Ogre creature token")));
    }
}

[
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return permanent.isEnemy(creature) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Put a 3/3 red Ogre creature token onto the battlefield unless your opponent pays {3}."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player=event.getPlayer();
            game.addEvent(new MagicEvent(
                event.getPermanent(),
                player.getOpponent(),
                new MagicMayChoice(
                    new MagicPayManaCostChoice(MagicManaCost.create("{3}"))
                ),
                player,
                action,
                "PN may\$ pay {3}."
            ));
        }
    }
]
