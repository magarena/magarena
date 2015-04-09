[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int x = payedCost.getX();
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE_WITHOUT_FLYING_YOU_DONT_CONTROL,
                new MagicDamageTargetPicker(x),
                x,
                this,
                "SN deals " + x + " damage to target creature\$ without flying you don't control."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                game.doAction(new MagicDealDamageAction(event.getSource(),it,event.getRefInt()));
            });
        }
    },
    new MagicOverloadActivation(MagicTiming.Removal) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            return [
                new MagicPayManaCostEvent(source,"{X}{X}{R}{R}")
            ];
        }
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int x = payedCost.getX();
            return new MagicEvent(
                cardOnStack,
                x,
                this,
                "SN deals " + x + " damage to each creature\$ without flying you don't control."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets=
                game.filterPermanents(event.getPlayer(),CREATURE_WITHOUT_FLYING_YOUR_OPPONENT_CONTROLS);
            final MagicSource source = event.getSource();
            final int amount = event.getRefInt();
            for (final MagicPermanent target : targets) {
                game.doAction(new MagicDealDamageAction(source,target,amount));
            }
        }
    }
]
