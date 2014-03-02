def CREATURE_OR_AURA_SPELL = new MagicStackFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack target) {
        return target.isSpell() && (target.hasType(MagicType.Creature) || target.hasSubType(MagicSubType.Aura));
    }
};

def NEG_TARGET_CREATURE_OR_AURA_SPELL = new MagicTargetChoice(
    CREATURE_OR_AURA_SPELL,
    MagicTargetHint.Negative,
    "target creature or Aura spell"
);

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE_OR_AURA_SPELL,
                this,
                "Counter target creature or Aura spell\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game, {
                final MagicCardOnStack targetSpell ->
                game.doAction(new MagicCounterItemOnStackAction(targetSpell));
            });
        }
    }
]
