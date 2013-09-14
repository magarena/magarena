[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.getSource().isFriend(permanent) &&
                    damage.getSource().isPermanent() &&
                    damage.getSource().hasSubType(MagicSubType.Warrior) &&
                    damage.isCombat() &&
                    damage.getTarget().isCreature()) ?
                new MagicEvent(
                    permanent,
                    damage.getTarget(),
                    this,
                    "Destroy RN at end of combat."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processRefPermanent(game, {
                final MagicPermanent permanent ->
                game.doAction(new MagicAddTriggerAction(
                    permanent,
                    MagicAtEndOfCombatTrigger.Destroy
                ))
            } as MagicPermanentAction);
        }
    }
]
