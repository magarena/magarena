def INSTANT_YOU_CONTROL_WITH_CMC_LEQ_2 = new MagicStackFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack target) {
        return target.getController() == player && target.isSpell(MagicType.Instant) && target.getConvertedCost() <= 2;
    }
};
def AN_INSTANT_YOU_CONTROL_WITH_CMC_LEQ_2 = new MagicTargetChoice(
    INSTANT_YOU_CONTROL_WITH_CMC_LEQ_2,
    MagicTargetHint.Positive,
    "an instant spell you control with converted mana cost 2 or less"
);
def SORCERY_YOU_CONTROL_WITH_CMC_LEQ_2 = new MagicStackFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack target) {
        return target.getController() == player && target.isSpell(MagicType.Sorcery) && target.getConvertedCost() <= 2;
    }
};
def A_SORCERY_YOU_CONTROL_WITH_CMC_LEQ_2 = new MagicTargetChoice(
    SORCERY_YOU_CONTROL_WITH_CMC_LEQ_2,
    MagicTargetHint.Positive,
    "a sorcery spell you control with converted mana cost 2 or less"
);
[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Spell),
        "Copy Instant"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source,"{2}{U}")];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                AN_INSTANT_YOU_CONTROL_WITH_CMC_LEQ_2,
                this,
                "Copy target instant spell\$ you control with converted mana cost 2 or less. You may choose new targets for the copy."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game, {
                game.doAction(new MagicCopyCardOnStackAction(event.getPlayer(),it));  
            });
        }
    },
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Spell),
        "Copy Sorcery"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source,"{2}{R}")];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                A_SORCERY_YOU_CONTROL_WITH_CMC_LEQ_2,
                this,
                "Copy target sorcery spell\$ you control with converted mana cost 2 or less. You may choose new targets for the copy."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game, {
                game.doAction(new MagicCopyCardOnStackAction(event.getPlayer(),it));  
            });
        }
    }    
]
