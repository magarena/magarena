[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.getSource().isFriend(permanent) &&
                    damage.getSource().isPermanent() &&
                    damage.getSource().hasSubType(MagicSubType.Warrior) &&
                    damage.isCombat() &&
                    damage.isTargetCreature()) ?
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
                game.doAction(new AddTurnTriggerAction(
                    it,
                    AtEndOfCombatTrigger.Destroy
                ))
            });
        }
    }
]
