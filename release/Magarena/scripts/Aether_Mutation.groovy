[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                MagicBounceTargetPicker.create(),
                this,
                "Return target creature\$ to its owner's hand. " +
                "PN creates X 1/1 green Saproling creature tokens, where X is that creature's converted mana cost."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new RemoveFromPlayAction(it,MagicLocationType.OwnersHand));
                game.doAction(new PlayTokensAction(
                    event.getPlayer(),
                    CardDefinitions.getToken("1/1 green Saproling creature token"),
                    it.getConvertedCost()
                ));
            });
        }
    }
]
