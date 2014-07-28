def NON_FAERIE_SPELL = new MagicStackFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack target) {
        return target.isSpell() && (target.hasSubType(MagicSubType.Faerie) == false);
    }
};

def NEG_TARGET_NON_FAERIE_SPELL = new MagicTargetChoice(
    NON_FAERIE_SPELL,
    MagicTargetHint.Negative,
    "target non-Faerie spell"
);

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_NON_FAERIE_SPELL,
                this,
                "Counter target non-Faerie spell\$. " +
                "If that spell is countered this way, exile it instead of putting it into its owner's graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game, {
                game.doAction(new MagicCounterItemOnStackAction(it, MagicLocationType.Exile));
            });
        }
    }
]
