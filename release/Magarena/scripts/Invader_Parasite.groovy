[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {  
            return new MagicEvent(
                permanent,
                MagicTargetChoice.NEG_TARGET_LAND,
                MagicExileTargetPicker.create(),
                this,
                "PN exiles target land\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent target ->
                game.doAction(new MagicExileLinkAction(
                    event.getPermanent(),
                    target
                ));
            });
            
        }
    },
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent otherPermanent) {
            final String cardName = permanent.getExiledCard().getName();
            return (otherPermanent.isLand() && 
                    otherPermanent.getName().equals(cardName) &&
                    otherPermanent.isEnemy(permanent)) ?
                new MagicEvent(
                    permanent,
                    otherPermanent.getController(),
                    this,
                    "SN deals 2 damage to PN."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicDamage damage = new MagicDamage(
                event.getSource(),
                event.getPlayer(),
                2
            );
            game.doAction(new MagicDealDamageAction(damage));
        }
    }
]
