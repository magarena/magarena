[
    new ClashTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer winner) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(new MagicPayManaCostChoice(MagicManaCost.create("{1}"))),
                winner,
                this,
                "PN may\$ pay {1}. If he or she does, PN creates a 3/1 red Elemental Shaman creature token. " +
                "If PN won the clash, that token gains haste until end of turn. "
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new PlayTokenAction(
                    event.getPlayer(),
                    CardDefinitions.getToken("3/1 red Elemental Shaman creature token"),
                    (event.getRefPlayer() == event.getPlayer()) ? [MagicPlayMod.HASTE_UEOT] : [MagicPlayMod.NONE]
                ));
            }
        }
    }
]
