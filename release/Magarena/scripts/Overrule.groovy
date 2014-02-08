[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_SPELL,
                payedCost.getX(),
                this,
                "Counter target spell\$ unless its controller pays {RN}.\$ PN gains RN life."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game, {
                final MagicCardOnStack targetSpell ->
                game.addEvent(new MagicCounterUnlessEvent(
                    event.getSource(), 
                    targetSpell, 
                    MagicManaCost.create("{" + event.getRefInt() + "}")
                ));
                game.doAction(new MagicChangeLifeAction(event.getPlayer(),event.getRefInt()));
            });
        }
    }
]
