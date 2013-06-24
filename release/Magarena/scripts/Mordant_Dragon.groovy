[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final int amount=damage.getAmount();
            return (damage.getSource() == permanent &&
                    damage.getTarget().isPlayer() &&
                    damage.isCombat()) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(MagicTargetChoice.TARGET_CREATURE_YOUR_OPPONENT_CONTROLS),
                    new MagicDamageTargetPicker(amount),
                    amount,
                    this,
                    "PN may\$ have SN deal RN damage to target creature\$ your opponent controls."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent creature) {
                        final MagicDamage damage=new MagicDamage(event.getSource(),creature,event.getRefInt());
                        game.doAction(new MagicDealDamageAction(damage));
                    }
                });
            }
        }
    }
]
