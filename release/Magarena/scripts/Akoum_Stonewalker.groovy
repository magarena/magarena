[
    new OtherEntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.isLand() && otherPermanent.isFriend(permanent)) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        new MagicPayManaCostChoice(MagicManaCost.create("{2}{R}"))
                    ),
                    this,
                    "PN may\$ pay {2}{R}. If he or she does, put a 3/1 red Elemental creature token with trample and haste onto "+
                    "the battlefield. Exile that token at the beginning of the next end step."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new PlayTokenAction(
                    event.getPlayer(),
                    CardDefinitions.getToken("3/1 red Elemental creature token with trample and haste"),
                    MagicPlayMod.EXILE_AT_END_OF_TURN
                ));
            }
        }
    }
]
