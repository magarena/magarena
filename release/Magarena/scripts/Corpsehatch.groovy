[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_NONBLACK_CREATURE,
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target nonblack creature\$. " +
                "PN puts two 0/1 colorless Eldrazi Spawn creature tokens onto the battlefield. " +
                "They have \"Sacrifice this creature: Add {1} to your mana pool.\""
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicDestroyAction(it));
                game.doAction(new MagicPlayTokensAction(
                    event.getPlayer(),
                    TokenCardDefinitions.get("0/1 colorless Eldrazi Spawn creature token"),
                    2
                ));
            });
        }
    }
]
