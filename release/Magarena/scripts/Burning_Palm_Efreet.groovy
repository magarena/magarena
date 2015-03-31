// May need further tweaking - Damage priority or lose evasion priority?
[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source, "{1}{R}{R}")];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_CREATURE_WITH_FLYING,
                new MagicDamageTargetPicker(2),
                this,
                "SN deals 2 damage to target creature with flying\$ and that creature loses flying until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicDealDamageAction(event.getSource(),it,2));
                game.doAction(new MagicLoseAbilityAction(it,MagicAbility.Flying));
            });
        }
    }
]
