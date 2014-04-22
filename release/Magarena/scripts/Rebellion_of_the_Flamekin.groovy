[
    new MagicWhenClashTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Boolean winClash) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(new MagicPayManaCostChoice(MagicManaCost.create("{1}"))),
                winClash ? 1 : 0,
                this,
                "PN may\$ pay {1}. If he or she does, PN puts a 3/1 red Elemental Shaman creature token onto the battlefield. " + 
                "If PN won the clash, that token gains haste until end of turn. "
            );
        }
        
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new MagicPlayTokenAction(
                    event.getPlayer(),
                    TokenCardDefinitions.get("3/1 red Elemental Shaman creature token"),
                    (event.getRefInt() == 1) ? [MagicPlayMod.HASTE_UEOT] : [MagicPlayMod.NONE]
                ));
            }
        }
    }
]
