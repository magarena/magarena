[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                payedCost.getKicker(),
                this,
                payedCost.isKicked() ?
                    "Destroy all creatures other than SN." :
                    "Destroy all creatures you control other than SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getRefInt() == 1) {
            final MagicTargetFilter<MagicPermanent> targetFilterkicked =
                new MagicOtherPermanentTargetFilter(
                MagicTargetFilterFactory.CREATURE,event.getPermanent());
            final Collection<MagicPermanent> targetskicked=
                game.filterPermanents(targetFilterkicked);
                    game.doAction(new MagicDestroyAction(targetskicked));
         
            } else {
            final MagicTargetFilter<MagicPermanent> targetFilternotkicked =
                new MagicOtherPermanentTargetFilter(
                MagicTargetFilterFactory.CREATURE_YOU_CONTROL,event.getPermanent());
            final Collection<MagicPermanent> targetsnotkicked=
                game.filterPermanents(event.getPlayer(),targetFilternotkicked);
                    game.doAction(new MagicDestroyAction(targetsnotkicked));
            }
        }
    }
]
