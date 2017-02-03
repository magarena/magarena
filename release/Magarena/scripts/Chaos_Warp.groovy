[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_PERMANENT,
                MagicExileTargetPicker.create(),
                this,
                "The owner of target permanent\$ shuffles it into his or her library, " +
                "then reveals the top card of his or her library. If it's a permanent card, "+
                "he or she puts it onto the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new RemoveFromPlayAction(it,MagicLocationType.OwnersLibrary));
                final MagicCard topCard = it.getOwner().getLibrary().getCardAtTop();
                game.doAction(new RevealAction(topCard));
                if (topCard.isPermanentCard()) {
                    game.doAction(new ReturnCardAction(MagicLocationType.OwnersLibrary, topCard, it.getOwner()));
                }
            });
        }
    }
]
