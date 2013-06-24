[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_CREATURE,
                MagicCopyTargetPicker.create(),
                this,
                "Put a token onto the battlefield that's a copy of target creature\$. " +
                "If SN was kicked, put five of those tokens onto the battlefield instead."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    final MagicPlayer player = event.getPlayer();
                    final MagicCardDefinition cardDefinition = creature.getCardDefinition();
                    int count = event.isKicked() ? 5 : 1;
                    for (;count>0;count--) {
                        game.doAction(new MagicPlayTokenAction(player,cardDefinition));
                    }
                }
            });
        }
    }
]
