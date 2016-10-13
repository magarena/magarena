[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE_WITH_FLYING,
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target creature with flying.\$ If that creature was blue or black, "+
                "PN creates a 1/2 green Spider creature token with reach."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new DestroyAction(it));
                if (it.hasColor(MagicColor.Blue) || it.hasColor(MagicColor.Black)) {
                    game.doAction(new PlayTokenAction(
                        event.getPlayer(),
                        CardDefinitions.getToken("1/2 green Spider creature token with reach")
                    ));
                }
            });
        }
    }
]
