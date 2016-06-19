[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN puts an X/X green Ooze creature token onto the battlefield, where X is the greatest power among creatures PN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            int x = 0;
            CREATURE_YOU_CONTROL.filter(event) each {
                x = Math.max(x,it.getPower());
            }
            game.logAppendValue(event.getPlayer(), x);
            game.doAction(new PlayTokenAction(event.getPlayer(), MagicCardDefinition.create(
                CardDefinitions.getToken("green Ooze creature token"),
                {
                    it.setPowerToughness(x, x);
                    it.setValue(x);
                }
            )));
        }
    }
]
