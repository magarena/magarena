[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN creates an X/X black and green Spirit Warrior creature token, where X is the greatest toughness among creatures PN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            int x = 0;
            CREATURE_YOU_CONTROL.filter(event) each {
                x = Math.max(x,it.getToughness());
            }
            game.logAppendValue(event.getPlayer(), x);
            game.doAction(new PlayTokenAction(
                event.getPlayer(),
                CardDefinitions.getToken(x, x, "black and green Spirit Warrior creature token")
            ));
        }
    }
]
