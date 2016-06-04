[
    new AttacksTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicPermanent attacker) {
            return permanent.getEquippedCreature() == attacker &&
                   attacker.getController().getNrOfAttackers() == 1;
        }
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent attacker) {
            return new MagicEvent(
                permanent,
                attacker,
                this,
                "RN gets +1/+1 until end of turn for each other creature PN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processRefPermanent(game, {
                final int amount = new MagicOtherPermanentTargetFilter(CREATURE_YOU_CONTROL, it).filter(event).size();
                game.logAppendMessage(event.getPlayer(), "(" + amount + ")");
                game.doAction(new ChangeTurnPTAction(it, amount, amount));
            });
        }
    }
]
