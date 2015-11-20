[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            final int amount = MagicCondition.SPELL_MASTERY_CONDITION.accept(cardOnStack) ? 3 : 2;
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                new MagicDamageTargetPicker(amount),
                this,
                "SN deals 2 damage to target creature\$. If there are two or more instant and/or sorcery cards "+
                "in PN's graveyard, SN deals 3 damage to that creature instead."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                final int amount = MagicCondition.SPELL_MASTERY_CONDITION.accept(event.getSource()) ? 3 : 2;
                game.doAction(new DealDamageAction(event.getSource(),it,amount));
                game.logAppendValue(event.getPlayer(),amount);
            });
        }
    }
]
