[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                payedCost.getX(),
                this,
                "Put RN RN/RN green Ooze creature tokens onto the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int x = event.getRefInt();
            game.doAction(new PlayTokensAction(
                event.getPlayer(),
                MagicCardDefinition.create(
                    CardDefinitions.getToken("green Ooze creature token"),
                    {
                        it.setPowerToughness(x, x);
                        it.setValue(x);
                    }
                ),
                x
            ));
        }
    }
]
