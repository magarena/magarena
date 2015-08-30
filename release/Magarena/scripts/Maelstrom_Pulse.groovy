[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_NONLAND_PERMANENT,
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target nonland permanent\$ and all other permanents with the same name as that permanent."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new DestroyAction(
                    new MagicNameTargetFilter(it.getName()).filter(event)
                ));
            });
        }
    }
]
