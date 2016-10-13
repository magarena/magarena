def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.isNo()) {
        game.doAction(new PlayTokenAction(event.getRefPlayer(),CardDefinitions.getToken("3/3 red Ogre creature token")));
    }
}

[
    new AttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return permanent.isEnemy(creature) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN creates a 3/3 red Ogre creature token unless their opponent pays {3}."
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
