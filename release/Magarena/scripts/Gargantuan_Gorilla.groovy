def SAC_ACTION = {
    final MagicGame game, final MagicEvent event ->
    event.processTargetPermanent(game, {
        game.doAction(new SacrificeAction(it));
        if (it.hasType(MagicType.Snow)) {
            game.doAction(new GainAbilityAction(event.getPermanent(),MagicAbility.Trample));
        }
    })
}

def choice = MagicTargetChoice.Negative("another target creature");

[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Sacrifice a Forest?"),
                this,
                "PN may\$ sacrifice a Forest. " +
                "If PN sacrifices a snow Forest this way, SN gains trample until end of turn. " +
                "If PN doesn't sacrifice a Forest, he or she sacrifices SN and it deals 7 damage to him or her."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent SN = event.getPermanent();
            final MagicPlayer PN = event.getPlayer();
            final MagicEvent sac = new MagicEvent(
                SN,
                PN,
                SACRIFICE_FOREST,
                MagicSacrificeTargetPicker.create(),
                SAC_ACTION,
                "Choose a Forest to sacrifice\$."
            );
            if (event.isYes() && PN.controlsPermanent(MagicSubType.Forest)) {
                game.addEvent(sac);
            } else {
                game.doAction(new SacrificeAction(SN));
                game.doAction(new DealDamageAction(SN, PN, 7));
            }
        }
    },
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                choice,
                new MagicDamageTargetPicker(source.getPower()),
                this,
                "SN deals damage equal to its power to another target creature. " +
                "That creature deals damage equal to its power to SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new DealDamageAction(
                    event.getPermanent(),
                    it,
                    event.getPermanent().getPower()
                ));
                game.doAction(new DealDamageAction(
                    it,
                    event.getPermanent(),
                    it.getPower()
                ));
            });
        }
    }
]
