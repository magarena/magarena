def DestroyTwin = new MagicPermanentActivation(
    new MagicActivationHints(MagicTiming.Removal),
    "Destroy"
) {
    @Override
    public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
        return [
            new MagicPayManaCostEvent(source,"{U}{B}"),
            new MagicTapEvent(source)
        ];
    }
    @Override
    public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
        final MagicTargetChoice targetChoice = new MagicTargetChoice(
            new MagicTargetFilter.NameTargetFilter(MagicTargetFilter.TARGET_CREATURE, source.getName()),
            MagicTargetHint.Negative,
            "target creature"
        );
        return new MagicEvent(
            source,
            targetChoice,
            new MagicDestroyTargetPicker(false),
            this,
            "Destroy target creature\$ with same name as this creature."
        );
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        event.processTargetPermanent(game,new MagicPermanentAction() {
            public void doAction(final MagicPermanent permanent) {
                game.doAction(new MagicDestroyAction(permanent));
            }
        });
    }
};
    
def GainAct = new MagicStatic(MagicLayer.Ability) {
    @Override
    public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
        permanent.addAbility(DestroyTwin);
    }
};

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicMayChoice(MagicTargetChoice.CREATURE),
                MagicCopyTargetPicker.create(),
                this,
                "Put SN onto the battlefield. You may\$ have SN enter the battlefield as a copy of any creature\$ on the battlefield, " + 
                "except it gains \"{U}{B}, {T}: Destroy target creature with the same name as this creature.\""
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    final MagicPermanent chosen ->
                    final MagicPlayCardFromStackAction action = new MagicPlayCardFromStackAction(
                        event.getCardOnStack(),
                        chosen.getCardDefinition()
                    );
                    game.doAction(action);
                    game.doAction(new MagicAddStaticAction(
                        action.getPermanent(),
                        GainAct
                    ));
                } as MagicPermanentAction);
            } else {
                game.doAction(new MagicPlayCardFromStackAction(
                    event.getCardOnStack()
                ));
            }
        }
    }
]
