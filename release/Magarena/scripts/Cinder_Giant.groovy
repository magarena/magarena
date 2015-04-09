[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "SN deals 2 damage to each other creature PN controls."
            );
        }
        
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent creature = event.getPermanent();
            final MagicTargetFilter<MagicPermanent> filter = CREATURE_YOU_CONTROL.except(creature);
            game.filterPermanents(event.getPlayer(),filter) each {
                game.doAction(new MagicDealDamageAction(creature,it,2));
            }
        }
    }
]
