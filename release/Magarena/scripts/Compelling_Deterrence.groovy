[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_NONLAND_PERMANENT,
                MagicBounceTargetPicker.create(),
                this,
                "Return target nonland permanent\$ to its owner's hand. "+
                "Then that player discards a card if PN controls a Zombie."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new RemoveFromPlayAction(it, MagicLocationType.OwnersHand));
                if (event.getPlayer().controlsPermanent(MagicSubType.Zombie)) {
                    game.addEvent(new MagicDiscardEvent(event.getSource(), it.getController()));
                }
            });
        }
    }
]
