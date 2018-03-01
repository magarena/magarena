[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE_YOU_CONTROL,
                this,
                "Exile target creature PN controls\$, then return that card to the battlefield under its owner's control. " +
                "If a Pirate was exiled this way, PN draws a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new RemoveFromPlayAction(it, MagicLocationType.Exile));
                game.doAction(new ReturnCardAction(MagicLocationType.Exile, it.getCard(), it.getOwner()));
                if (it.hasSubType(MagicSubType.Pirate)) {
                    game.doAction(new DrawAction(event.getPlayer()));
                }
            });
        }
    }
]

