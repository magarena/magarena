[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicSacrificeEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_CREATURE_WITH_FLYING,
                new MagicDamageTargetPicker(source.getPower()),
                this,
                "SN deals damage equal to SN's power to target creature with flying.\$"
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicDamage damage = new MagicDamage(
                    event.getSource(),
                    it,
                    event.getPermanent().getPower()
                );
                game.doAction(new MagicDealDamageAction(damage));
                game.logAppendMessage(event.getPlayer(),"("+event.getPermanent().getPower()+")");
            });
        }
    }
]
