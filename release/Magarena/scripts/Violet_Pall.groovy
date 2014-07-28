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
                "PN puts a 1/1 black Faerie Rogue creature token with flying onto the battlefield"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicDestroyAction(it));
                game.doAction(new MagicPlayTokenAction(
                    event.getPlayer(),
                    TokenCardDefinitions.get("1/1 black Faerie Rogue creature token with flying")
                ));
            });
        }
    }
]
