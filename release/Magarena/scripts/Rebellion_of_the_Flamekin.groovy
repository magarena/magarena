[
    new MagicWhenClashTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object winClash) {
            final int winInt = ((boolean)winClash) ? 1 : 0;
            return new MagicEvent(
                permanent,
                new MagicMayChoice(new MagicPayManaCostChoice(MagicManaCost.create("{1}"))),
                winInt,
                this,
                "PN may pay 1. If he or she does, PN puts a 3/1 red Elemental Shaman creature token onto the battlefield. If PN won the clash, that token gains haste until end of turn. "
            );

        }
        
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if(event.isYes()) {
                if(event.getRefInt() == 1) game.doAction(new MagicPlayTokenAction(event.getPlayer(),TokenCardDefinitions.get("3/1 red Elemental Shaman creature token"), MagicPlayMod.HASTE_UEOT));
                else game.doAction(new MagicPlayTokenAction(event.getPlayer(),TokenCardDefinitions.get("3/1 red Elemental Shaman creature token")));
            }
        }
    }
]
