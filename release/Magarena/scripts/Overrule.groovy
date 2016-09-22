[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_SPELL,
                payedCost.getX(),
                this,
                "Counter target spell\$ unless its controller pays {RN}. PN gains RN life."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game, {
                game.addEvent(new MagicCounterUnlessEvent(
                    event.getSource(),
                    it,
                    MagicManaCost.create(event.getRefInt())
                ));
                game.doAction(new ChangeLifeAction(event.getPlayer(),event.getRefInt()));
            });
        }
    }
]
