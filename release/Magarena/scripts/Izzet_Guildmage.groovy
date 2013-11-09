def TARGET_INSTANT_YOU_CONTROL_WITH_CMC_LEQ_2 = new MagicStackFilterImpl() {
	public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack target) {
		return target.getController() == player && target.isSpell(MagicType.Instant) && target.getConvertedCost() <= 2;
	}
};
def INSTANT_YOU_CONTROL_WITH_CMC_LEQ_2 = new MagicTargetChoice(
    TARGET_INSTANT_YOU_CONTROL_WITH_CMC_LEQ_2,
    MagicTargetHint.Positive,
    "an instant spell with converted mana cost 2 or less you control"
);
def TARGET_SORCERY_YOU_CONTROL_WITH_CMC_LEQ_2 = new MagicStackFilterImpl() {
	public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack target) {
		return target.getController() == player && target.isSpell(MagicType.Sorcery) && target.getConvertedCost() <= 2;
	}
};
def SORCERY_YOU_CONTROL_WITH_CMC_LEQ_2 = new MagicTargetChoice(
    TARGET_SORCERY_YOU_CONTROL_WITH_CMC_LEQ_2,
    MagicTargetHint.Positive,
    "a sorcery spell with converted mana cost 2 or less you control"
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
                INSTANT_YOU_CONTROL_WITH_CMC_LEQ_2,
                this,
                "Copy target instant\$ with converted mana cost 2 or less you control. You may choose new targets for the copy."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game,new MagicCardOnStackAction() {
                public void doAction(final MagicCardOnStack targetSpell) {
                    game.doAction(new MagicCopyCardOnStackAction(event.getPlayer(),targetSpell));  
                }
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
                SORCERY_YOU_CONTROL_WITH_CMC_LEQ_2,
                this,
                "Copy target sorcery\$ with converted mana cost 2 or less you control. You may choose new targets for the copy."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game,new MagicCardOnStackAction() {
                public void doAction(final MagicCardOnStack targetSpell) {
                    game.doAction(new MagicCopyCardOnStackAction(event.getPlayer(),targetSpell));  
                }
            });
        }
    }	
]
