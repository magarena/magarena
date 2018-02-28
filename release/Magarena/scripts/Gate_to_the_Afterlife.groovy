def condition = {
    final MagicSource source ->
    return source.getController().getGraveyard().count({ it.hasType(MagicType.Creature) }) >= 6;
}

def filter = new MagicCardFilterImpl() {
    @Override
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
        return target.getName().equalsIgnoreCase("God-Pharaoh's Gift");
    }
    @Override
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType == MagicTargetType.Graveyard ||
            targetType == MagicTargetType.Hand ||
            targetType == MagicTargetType.Library;
    }
}

[
    new MagicPermanentActivation(
        [condition]
        new MagicActivationHints(MagicTiming.Token),
        "God-Pharaoh's Gift"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{2}"),
                new MagicTapEvent(source),
                new MagicSacrificeEvent(source)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN searches PN's graveyard, hand, and/or library for a card named God-Pharaoh's Gift and put it onto the battlefield. " +
                "If PN searches PN's library this way, shuffle it."
            );
        }
        @Override
        public MagicEvent executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSearchOntoBattlefieldEvent(
                event,
                new MagicFromCardFilterChoice(
                    filter,
                    1,
                    false,
                    "a card named God-Pharaoh's Gift from your graveyard, hand, and/or library"
                )
            ));
        }
    }
]

