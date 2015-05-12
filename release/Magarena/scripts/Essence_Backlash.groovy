[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE_SPELL,
                this,
                "Counter target creature spell.\$ SN deals damage equal to that spell's power to its controller."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game, {
                final int amount = it.getCard().getPower();
                game.logAppendMessage(event.getPlayer(),"("+amount+")");
                game.doAction(new CounterItemOnStackAction(it));
                game.doAction(new DealDamageAction(event.getSource(),it.getController(),amount));
            });
        }
    }
]
