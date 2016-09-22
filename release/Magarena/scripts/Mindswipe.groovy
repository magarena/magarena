[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_SPELL,
                payedCost.getX(),
                this,
                "Counter target spell\$ unless its controller pays {X}. "+
                "SN deals X damage to that spell's controller. (X=RN)"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game, {
                final int amount = event.getRefInt();
                game.addEvent(new MagicCounterUnlessEvent(
                    event.getSource(),
                    it,
                    MagicManaCost.create(amount)
                ));
                game.doAction(new DealDamageAction(event.getSource(), it.getController(), amount));
            });
        }
    }
]
