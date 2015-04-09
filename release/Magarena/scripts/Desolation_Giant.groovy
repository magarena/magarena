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
                final MagicTargetFilter<MagicPermanent> targetFilterkicked = CREATURE.except(event.getPermanent());
                final Collection<MagicPermanent> targetsKicked = game.filterPermanents(targetFilterkicked);
                game.doAction(new MagicDestroyAction(targetsKicked));
            } else {
                final MagicTargetFilter<MagicPermanent> targetFilterNotkicked = CREATURE_YOU_CONTROL.except(event.getPermanent());
                final Collection<MagicPermanent> targetsNotKicked = game.filterPermanents(event.getPlayer(),targetFilterNotkicked);
                game.doAction(new MagicDestroyAction(targetsNotKicked));
            }
        }
    }
]
