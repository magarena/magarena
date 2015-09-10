[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target creature\$. If there are two or more instant and/or sorcery cards "+
                "in PN's graveyard, PN gains 2 life."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new DestroyAction(it));
                if (MagicCondition.SPELL_MASTERY_CONDITION.accept(event.getSource())) {
                    game.doAction(new ChangeLifeAction(event.getPlayer(),2));
                }
            });
        }
    }
]
