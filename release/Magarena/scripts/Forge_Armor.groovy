[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                POS_TARGET_CREATURE,
                MagicPumpTargetPicker.create(),
                payedCost.getTarget(),
                this,
                "PN puts X +1/+1 counters on target creature, where X is RN's converted mana cost."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final int amount=event.getRefPermanent().getConvertedCost();
                game.logAppendValue(event.getPlayer(),amount);
                game.doAction(new ChangeCountersAction(event.getPlayer(), it, MagicCounterType.PlusOne, amount));
            });
        }
    }
]
