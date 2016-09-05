def dealDamage = new DamageIsDealtTrigger() {
    @Override
    public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
        return damage.getTarget() == permanent ?
            new MagicEvent(
                permanent,
                damage.getAmount(),
                this,
                "SN deals RN damage to each other creature and each player."
            ):
            MagicEvent.NONE;
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        final int amount = event.getRefInt();
        CREATURE.except(event.getPermanent()).filter(event) each {
            game.doAction(new DealDamageAction(event.getSource(), it, amount));
        }
        game.getAPNAP() each {
            game.doAction(new DealDamageAction(event.getSource(), it, amount));
        }
    }
};

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE,
                this,
                "Whenever target creature\$ is dealt damage this turn, it deals that much damage to each other creature and each player."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new AddTurnTriggerAction(it, dealDamage));
            });
        }
    }
]
