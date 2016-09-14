def DestroyTwin = new MagicPermanentActivation(
    new MagicActivationHints(MagicTiming.Removal),
    "Destroy"
) {
    @Override
    public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
        return [
            new MagicPayManaCostEvent(source,"{U}{B}"),
            new MagicTapEvent(source)
        ];
    }
    @Override
    public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
        final MagicTargetChoice targetChoice = new MagicTargetChoice(
            new MagicNameTargetFilter(CREATURE, source.getName()),
            MagicTargetHint.Negative,
            "target creature"
        );
        return new MagicEvent(
            source,
            targetChoice,
            MagicDestroyTargetPicker.Destroy,
            this,
            "Destroy target creature\$ with same name as this creature."
        );
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        event.processTargetPermanent(game, {
            game.doAction(new DestroyAction(it));
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
                new MagicMayChoice(A_CREATURE),
                MagicCopyPermanentPicker.create(),
                this,
                "PN may\$ have SN enter the battlefield as a copy of any creature on the battlefield\$, " +
                "except it gains \"{U}{B}, {T}: Destroy target creature with the same name as this creature.\""
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new EnterAsCopyAction(event.getCardOnStack(), event.getTarget(), {
                    final MagicPermanent perm ->
                    final MagicGame G = perm.getGame();
                    G.doAction(new AddStaticAction(
                        perm,
                        GainAct
                    ));
                }));
            } else {
                game.doAction(new PlayCardFromStackAction(
                    event.getCardOnStack()
                ));
            }
        }
    }
]
