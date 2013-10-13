[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_CREATURE,
                MagicExileTargetPicker.create(),
                this,
                "Exile target creature\$. Its controller puts a 1/1 colorless Shapeshifter " +
                " creature token with changeling onto the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicRemoveFromPlayAction(creature,MagicLocationType.Exile));
                    game.doAction(new MagicPlayTokensAction(
                        creature.getController(),
                        TokenCardDefinitions.get("Changeling"),
                        1
                    ));
                }
            });
        }
    }
]
