[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_PLAYER,
                new MagicDamageTargetPicker(1),
                this,
                "SN deals 1 damage to target player\$. " +
                "SN deals 3 damage to that player instead if PN controls " +
                "artifacts named Crown of Empires and Throne of Empires."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicTargetFilter<MagicPermanent> crown = new MagicNameTargetFilter("Crown of Empires");
            final MagicTargetFilter<MagicPermanent> throne = new MagicNameTargetFilter("Throne of Empires");
            final MagicSource source = event.getSource();
            final MagicPlayer player = source.getController();
            final int amount = (player.controlsPermanent(crown) && player.controlsPermanent(throne))? 3 : 1;
            event.processTarget(game, {
                game.doAction(new DealDamageAction(source,it,amount));
            });
        }
    }
]
