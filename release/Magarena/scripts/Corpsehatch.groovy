[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_NONBLACK_CREATURE,
                new MagicDestroyTargetPicker(false),
                this,
                "Destroy target nonblack creature\$. " +
                "Put two 0/1 colorless Eldrazi Spawn creature tokens onto the battlefield. " +
                "They have \"Sacrifice this creature: Add {1} to your mana pool.\""
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicDestroyAction(creature));
                    game.doAction(new MagicPlayTokensAction(
                        event.getPlayer(),
                        TokenCardDefinitions.get("Eldrazi Spawn"),
                        2
                    ));
                }
            });
        }
    }
]
