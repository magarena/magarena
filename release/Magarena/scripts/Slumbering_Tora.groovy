def SPIRIT_OR_ARCANE_CARD_FROM_HAND = new MagicCardFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
        return target.hasSubType(MagicSubType.Spirit) || target.hasSubType(MagicSubType.Arcane);
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType == MagicTargetType.Hand;
    }
}

def A_SPIRIT_OR_ARCANE_CARD_FROM_HAND = new MagicTargetChoice(
    SPIRIT_OR_ARCANE_CARD_FROM_HAND,
    "a Spirit or Arcane card"
)

def PT = {
    final int amount ->
    return new MagicStatic(MagicLayer.SetPT, MagicStatic.UntilEOT) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
            pt.set(amount,amount);
        }
    };
}

def ST = new MagicStatic(MagicLayer.Type, MagicStatic.UntilEOT) {
    @Override
    public void modSubTypeFlags(final MagicPermanent permanent, final Set<MagicSubType> flags) {
        flags.add(MagicSubType.Cat);
    }
    @Override
    public int getTypeFlags(final MagicPermanent permanent,final int flags) {
        return flags|MagicType.Creature.getMask()|MagicType.Artifact.getMask();
    }
};

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Animate),
        "Becomes"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{2}"),
                new MagicDiscardChosenEvent(source, A_SPIRIT_OR_ARCANE_CARD_FROM_HAND)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                payedCost.getTarget(),
                this,
                "SN becomes an X/X Cat artifact creature until end of turn, where X is the discarded card's converted mana cost."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getRefCard().getConvertedCost();
            game.logAppendX(event.getPlayer(), amount)
            game.doAction(new BecomesCreatureAction(event.getPermanent(), PT(amount), ST));
        }
    }
]
