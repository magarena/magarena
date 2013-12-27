[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_INSTANT_OR_SORCERY_SPELL,
                this,
                "Counter target instant or sorcery spell\$ unless its controller pays {1}. " +
                "Draw a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game, {
                final MagicCardOnStack targetSpell ->
                game.addEvent(new MagicCounterUnlessEvent(event.getSource(),targetSpell,MagicManaCost.create("{1}")));
                game.doAction(new MagicDrawAction(event.getPlayer()));
            });
        }
    }
]
