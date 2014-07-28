[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_SPELL,
                this,
                "Counter target spell\$ unless its controller pays {4}. " +
                "PN gains 4 life."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game, {
                game.addEvent(new MagicCounterUnlessEvent(event.getSource(),it,MagicManaCost.create("{4}")));
                game.doAction(new MagicChangeLifeAction(event.getPlayer(),4));
            });
        }
    }
]
