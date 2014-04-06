def NONTOKEN_CREATURE_YOU_CONTROL = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
        return target.isController(player) &&
               target.isCreature() &&
               target.isNonToken();
    }
};
def SACRIFICE_NONTOKEN = new MagicTargetChoice(
    NONTOKEN_CREATURE_YOU_CONTROL,
    MagicTargetHint.Positive,
    "a nontoken creature"
);
[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Token),
        "Token"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{2}{B}{G}"),
                new MagicSacrificePermanentEvent(source, SACRIFICE_NONTOKEN)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                source.getController(),
                payedCost.getTarget(),
                this,
                "PN puts X 1/1 green Saproling creature tokens onto the battlefield, where X is the sacrificed creature\$'s toughness."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent sacrificed = event.getRefPermanent();
            game.doAction(new MagicPlayTokensAction(event.getPlayer(), TokenCardDefinitions.get("1/1 green Saproling creature token"), sacrificed.getToughness()));
        }
    }
]
