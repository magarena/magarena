[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Token),
        "Token"
    ) {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            final MagicTargetFilter<MagicCard> targetFilter =
                    new MagicTargetFilter.MagicCMCCardFilter(
                        MagicTargetFilter.TARGET_CREATURE_CARD_FROM_HAND,
                        MagicTargetFilter.Operator.EQUAL,
                        source.getCounters(MagicCounterType.Charge)
                    );
            final MagicTargetChoice targetChoice =
                    new MagicTargetChoice(
                    targetFilter,false,MagicTargetHint.None,"a creature card from your hand");
            return new MagicEvent(
                source,
                targetChoice,
                new MagicGraveyardTargetPicker(true),
                this,
                "Put a creature card\$ with converted mana cost equal to " +
                "the number of charge counters on SN from your hand onto the battlefield."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game,new MagicCardAction() {
                public void doAction(final MagicCard card) {
                    game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersHand));
                    game.doAction(new MagicPlayCardAction(card,event.getPlayer()));
                }
            });
        }
    }
]
