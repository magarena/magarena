def TARGET_NONTOKEN_CREATURE_YOU_CONTROL = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
        return target.isController(player) &&
               target.isCreature() &&
               !target.isToken();
    }
};
def SACRIFICE_NONTOKEN = new MagicTargetChoice(
    TARGET_NONTOKEN_CREATURE_YOU_CONTROL,
    MagicTargetHint.Positive,
    "a nontoken creature"
);
[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Pump"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{1}{B}{G}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.POS_TARGET_CREATURE,
                MagicPumpTargetPicker.create(),
                this,
                "Target creature\$ gets +1/+1 and gains intimidate until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicChangeTurnPTAction(creature,1,1));
                    game.doAction(new MagicGainAbilityAction(creature, MagicAbility.Intimidate));
                }
            });
        }
    },
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
