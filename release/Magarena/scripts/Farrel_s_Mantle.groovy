def EXCEPT_ENCHANTED = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
        return target.isCreature() && target != source;
    }
};

def NEG_TARGET_CREATURE_EXCEPT = {
    final MagicSource source ->
    return new MagicTargetChoice(
        EXCEPT_ENCHANTED,
        MagicTargetHint.Negative,
        "a permanent except ${source.getName()}"
    );
};

[
    new MagicWhenAttacksUnblockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            final MagicPermanent enchanted = permanent.getEnchantedPermanent();
            return (creature == enchanted) ?
                new MagicEvent(
                    permanent,
                    enchanted.getController(),
                    new MagicMayChoice(NEG_TARGET_CREATURE_EXCEPT(enchanted)),
                    new MagicDamageTargetPicker(2 + enchanted.getPower()),
                    this,
                    "PN may\$ have enchanted creature (${enchanted.getName()}) deal damage equal to its power plus 2 "+
                    "to another target creature.\$ If that player does, the attacking creature assigns no combat damage this turn."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    final MagicPermanent enchanted = event.getPermanent().getEnchantedPermanent();
                    final int amount = enchanted.getPower()+2;
                    game.logAppendValue(event.getPlayer(), amount);
                    game.doAction(new DealDamageAction(enchanted, it, amount));
                    game.doAction(ChangeStateAction.Set(enchanted, MagicPermanentState.NoCombatDamage));
                });
            }
        }
    }
]
