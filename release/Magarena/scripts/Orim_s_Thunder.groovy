def action = {
    final MagicGame game, final MagicEvent event ->
    event.processTarget(game,new MagicTargetAction() {
        public void doAction(final MagicTarget target) {
            final MagicDamage damage = new MagicDamage(event.getSource(),target,event.getRefInt());
            game.doAction(new MagicDealDamageAction(damage));
        }
    });
} as MagicEventAction

[ 
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicKickerChoice(
                    MagicTargetChoice.NEG_TARGET_ARTIFACT_OR_ENCHANTMENT,
                    MagicManaCost.create("{R}"),
                    false
                ),
                new MagicDestroyTargetPicker(false),
                this,
                "Destroy target artifact or enchantment\$." + 
                "If SN was kicked\$, " + 
                "it deals damage equal to that permanent's converted mana cost to target creature."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent target) {
                    game.doAction(new MagicDestroyAction(target));
                    if (!event.isKicked()) {
                        return;
                    }
                    final int amount = target.getConvertedCost();
                    game.addEvent(new MagicEvent(
                        event.getSource(),
                        MagicTargetChoice.NEG_TARGET_CREATURE,
                        new MagicDamageTargetPicker(amount),
                        amount,
                        action,
                        "SN deals RN damage to target creature\$."
                    ));
                }
            });
        }
    }
]
