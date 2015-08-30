[
    new MagicPlaneswalkerActivation(1) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Put a 3/3 green Beast creature token onto the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PlayTokenAction(
                event.getPlayer(),
                CardDefinitions.getToken("3/3 green Beast creature token")
            ));
        }
    },
    new MagicPlaneswalkerActivation(-3) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Draw cards equal to the greatest power among creatures you control."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            int power = 0;
            CREATURE_YOU_CONTROL.filter(event) each {
                power = Math.max(power, it.getPower());
            }
            game.doAction(new DrawAction(
                event.getPlayer(),
                power
            ));
        }
    },
    new MagicPlaneswalkerActivation(-6) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Put a 6/6 green Wurm creature token onto the battlefield for each land you control."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amt = event.getPlayer().getNrOfPermanents(MagicType.Land);
            game.doAction(new PlayTokensAction(
                event.getPlayer(),
                CardDefinitions.getToken("6/6 green Wurm creature token"),
                amt
            ));
        }
    }
]
