[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {  
			return new MagicEvent(
                permanent,
                MagicTargetChoice.TARGET_LAND,
                MagicExileTargetPicker.create(),
                this,
                "PN exiles target land\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent source = event.getPermanent();
			final MagicCardList exiled = source.getExiledCards();			
			event.processTargetPermanent(game,new MagicPermanentAction() {
				public void doAction(final MagicPermanent target) {
					game.doAction(new MagicRemoveFromPlayAction(target, MagicLocationType.Exile));
					exiled.addToTop(target.getCard());
				}
			});
            
        }
    },
	new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent otherPermanent) {
            final MagicCardList exiled = permanent.getExiledCards();
			final MagicCard card = exiled.getCardAtTop();
			return (otherPermanent.isLand() && otherPermanent.isEnemy(permanent) && otherPermanent.getName() == card.getName()) ?
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
