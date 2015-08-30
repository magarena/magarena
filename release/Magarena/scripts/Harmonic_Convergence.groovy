[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Put all enchantments on top of their owners' libraries."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new RemoveAllFromPlayAction(
                ENCHANTMENT.filter(event),
                MagicLocationType.TopOfOwnersLibrary
            ));
        }
    }
]
