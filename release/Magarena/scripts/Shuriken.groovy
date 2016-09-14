[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent permanent) {
            return [
                new MagicTapEvent(permanent.getEquippedCreature())
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent permanent,final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                NEG_TARGET_CREATURE,
                permanent.getEquippedCreature(),
                this,
                "Unattach SN. SN deals 2 damage to target creature\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event){
            final MagicPermanent source = event.getPermanent();
            final MagicPermanent holder = event.getRefPermanent();
            if (source.getEquippedCreature() == holder) {
                game.doAction(new AttachAction(source, MagicPermanent.NONE));
                event.processTarget(game,{
                    game.doAction(new DealDamageAction(source,it,2));
                    if (holder.hasSubType(MagicSubType.Ninja) == false) {
                        game.doAction(new GainControlAction(it.getController(),source));
                    }
                });
            }
        }
    }
]
