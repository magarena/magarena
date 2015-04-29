[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.MustAttack),
        "Attacks"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_PLAYER,
                this,
                "Exile the top card of target player's\$ library. " +
                "If it's a land card, PN gains 1 life."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicCard card = it.getLibrary().getCardAtTop();
                game.doAction(new RemoveCardAction(card,MagicLocationType.OwnersLibrary));
                game.doAction(new MoveCardAction(card,MagicLocationType.OwnersLibrary,MagicLocationType.Exile));
                if (card.hasType(MagicType.Land)) {
                    game.doAction(new ChangeLifeAction(event.getPlayer(),1));
                }
            });
        }
    }
]
