[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Tap"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source), new MagicPayManaCostEvent(source, "{B}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_CREATURE,
                MagicTapTargetPicker.Tap,
                this,
                "Tap target creature.\$ SN deals damage equal to SN's power to that creature."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicTapAction(it));
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
