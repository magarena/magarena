[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                MagicExileTargetPicker.create(),
                this,
                "Exile target creature\$. " +
                "Its controller puts a 1/1 colorless Shapeshifter creature token with changeling onto the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicRemoveFromPlayAction(it,MagicLocationType.Exile));
                game.doAction(new PlayTokenAction(
                    it.getController(),
                    TokenCardDefinitions.get("1/1 colorless Shapeshifter creature token with changeling")
                ));
            });
        }
    }
]
