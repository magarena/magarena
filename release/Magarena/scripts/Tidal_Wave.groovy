[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Put a 5/5 blue Wall creature token with defender onto the battlefield. " + 
                "Sacrifice it at the beginning of the next end step."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicPlayTokenAction(
                event.getPlayer(),
                TokenCardDefinitions.get("5/5 blue Wall creature token with defender"),
                [MagicPlayMod.SACRIFICE_AT_END_OF_TURN]
            ));
        }
    }
]
