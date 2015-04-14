def choice = Negative("target artifact, creature, or enchantment");

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                choice,
                MagicExileTargetPicker.create(),
                this,
                "Put target artifact, creature, or enchantment\$ on the bottom of its owner's library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new RemoveFromPlayAction(it,MagicLocationType.BottomOfOwnersLibrary));
            });
        }
    }
]
