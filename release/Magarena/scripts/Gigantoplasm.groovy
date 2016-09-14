def PowerToughness = new MagicPermanentActivation(
    new MagicActivationHints(MagicTiming.Pump, true),
    "X/X"
) {
    @Override
    public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
        return [
            new MagicPayManaCostEvent(source,"{X}"),
            new MagicPlayAbilityEvent(source)
        ];
    }
    @Override
    public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
        final int x=payedCost.getX();
        return new MagicEvent(
            source,
            x,
            this,
            "SN has base power and toughness RN/RN."
        );
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        final int X = event.getRefInt();
        final MagicStatic PT = new MagicStatic(MagicLayer.SetPT) {
            @Override
            public void modPowerToughness(final MagicPermanent S, final MagicPermanent P, final MagicPowerToughness pt) {
                pt.set(X,X);
            }
        };
        game.doAction(new BecomesCreatureAction(event.getPermanent(),PT));
    }
};

def GainAct = new MagicStatic(MagicLayer.Ability) {
    @Override
    public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
        permanent.addAbility(PowerToughness);
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
                "except it gains \"{X}: This creature has base power and toughness X/X.\""
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
