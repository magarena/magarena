def Zombie = new MagicStatic(MagicLayer.Type) {
    @Override
    public void modSubTypeFlags(final MagicPermanent permanent,final Set<MagicSubType> flags) {
        flags.add(MagicSubType.Zombie);
    }
};

def Black = new MagicStatic(MagicLayer.Color) {
    @Override
    public int getColorFlags(final MagicPermanent permanent,final int flags) {
        return flags|MagicColor.Black.getMask();
    }
};
[
    new MagicPermanentActivation(
        [
            MagicCondition.CAN_TAP_CONDITION,
            MagicCondition.HAS_CARD_CONDITION,
            MagicConditionFactory.ManaCost("{1}")
        ],
        new MagicActivationHints(MagicTiming.Main,true),
        "Add counter"
    ) {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostTapEvent(source,"{1}"),
                new MagicDiscardEvent(source,source.getController(),1,false)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Put a study counter on SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeCountersAction(event.getPermanent(),MagicCounterType.Charge,1,true));
        }
    },
    new MagicPermanentActivation(
        [
            MagicCondition.CAN_TAP_CONDITION,
            MagicConditionFactory.ChargeCountersAtLeast(3)
        ],
        new MagicActivationHints(MagicTiming.Token),
        "Reanimate"
    ) {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicRemoveCounterEvent(source,MagicCounterType.Charge,3),
                new MagicSacrificeEvent(source)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Put all creature cards from all graveyards onto the " +
                "battlefield under your control. They're black Zombies " +
                "in addition to their other colors and types."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final Collection<MagicCard> targets =
                    game.filterCards(player,MagicTargetFilter.TARGET_CREATURE_CARD_FROM_ALL_GRAVEYARDS);
            for (final MagicTarget target : targets) {
                final MagicCard card = (MagicCard) target;
                if (card.getOwner().getGraveyard().contains(card)) {
                    final MagicPlayCardAction action = new MagicPlayCardAction(card,player,MagicPlayCardAction.NONE);
                    game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
                    game.doAction(action);

                    final MagicPermanent permanent = action.getPermanent();
                    game.doAction(new MagicAddStaticAction(permanent, Zombie));
                    game.doAction(new MagicAddStaticAction(permanent, Black));
                }
            }
        }
    }
]
