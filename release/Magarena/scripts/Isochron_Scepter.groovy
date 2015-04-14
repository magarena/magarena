def INSTANT_LEQ_CMC_2_FROM_HAND = new MagicCardFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
        return target.getConvertedCost() <= 2 && target.hasType(MagicType.Instant);
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType == MagicTargetType.Hand;
    }
};

[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            final MagicTargetChoice targetChoice = new MagicTargetChoice(
                INSTANT_LEQ_CMC_2_FROM_HAND,  
                MagicTargetHint.None,
                "an instant card with converted mana cost 2 or less to exile from your hand"
            );
            return new MagicEvent(
                permanent,
                new MagicMayChoice(targetChoice),
                MagicGraveyardTargetPicker.PutOntoBattlefield,
                this,
                "PN may\$ exile an instant card\$ with converted mana cost 2 or less from his or her hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetCard(game, {
                    game.doAction(new MagicExileLinkAction(
                        event.getPermanent(), 
                        it, 
                        MagicLocationType.OwnersHand
                    ));
                });
            }
        }
    },
    new MagicPermanentActivation(
        [MagicCondition.HAS_EXILED_CARD],
        new MagicActivationHints(MagicTiming.Removal),
        "Copy"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source), new MagicPayManaCostEvent(source,  "{2}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                source.getExiledCard(),
                this,
                "PN cast a copy of RN without paying its mana cost."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new CastFreeCopyAction(
                event.getPlayer(), 
                event.getRefCard()
            ));
        }
    }
]
