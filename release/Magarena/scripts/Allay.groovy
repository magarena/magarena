[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_ENCHANTMENT,
                new MagicDestroyTargetPicker(false),
                this,
                "Destroy target enchantment\$. " +
                "If the buyback cost was payed, return SN to its owner's hand as it resolves."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent permanent) {
                    game.doAction(new MagicDestroyAction(permanent));
                    if (event.isBuyback()) {
                        game.doAction(new MagicChangeCardDestinationAction(
                            event.getCardOnStack(),
                            MagicLocationType.OwnersHand
                        ));
                    }
                }
            });
        }
    }
]
