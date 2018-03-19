def effect = MagicRuleEventAction.create("target creature gets +1/+1 until end of turn for each Clue you control.")

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Investigate."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PlayTokensAction(event.getPlayer(), CardDefinitions.getToken("colorless Clue artifact token"),1));
                game.addEvent(effect.getEvent(event));
        }
    }
]
