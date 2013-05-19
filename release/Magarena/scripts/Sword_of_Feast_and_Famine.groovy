[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicPlayer player=permanent.getController();
            return (damage.getSource()==permanent.getEquippedCreature() && 
                    damage.getTarget().isPlayer() && 
                    damage.isCombat()) ?
                new MagicEvent(
                    permanent,
                    player,
                    damage.getTarget(),
                    this,
                    "RN discards a card and you untap all lands you control."
                ):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicDiscardEvent(event.getPermanent(),event.getRefPlayer(),1,false));
            final Collection<MagicPermanent> targets = 
                game.filterPermanents(event.getPlayer(),MagicTargetFilter.TARGET_LAND_YOU_CONTROL);
            for (final MagicPermanent target : targets) {
                game.doAction(new MagicUntapAction(target));
            }
        }
    }
]
