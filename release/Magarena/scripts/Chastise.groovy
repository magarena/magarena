[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_ATTACKING_CREATURE,
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target attacking creature\$. " +
                "PN gains life equal to its power."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final int power=it.getPower();
                game.doAction(new DestroyAction(it));
                game.doAction(new ChangeLifeAction(event.getPlayer(),power));
            });
        }
    }
]
