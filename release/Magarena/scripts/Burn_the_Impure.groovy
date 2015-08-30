[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                new MagicDamageTargetPicker(3),
                this,
                "SN deals 3 damage to target creature\$. If that creature " +
                "has infect, SN deals 3 damage to that creature's controller."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new DealDamageAction(event.getSource(),it,3));
                if (it.hasAbility(MagicAbility.Infect)) {
                    game.doAction(new DealDamageAction(event.getSource(),it.getController(),3));
                }
            });
        }
    }
]
