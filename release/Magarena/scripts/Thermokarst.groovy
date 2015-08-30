[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_LAND,
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target land.\$ If that land was snow, PN gains 1 life."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new DestroyAction(it));
                if (it.hasType(MagicType.Snow)) {
                    game.doAction(new ChangeLifeAction(event.getPlayer(), 1));
                }
            });
        }
    }
]
