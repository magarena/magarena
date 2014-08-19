package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicDrawAction;
import magic.model.action.MagicRemoveTriggerAction;
import magic.model.action.MagicTransformAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class MagicAtUpkeepTrigger extends MagicTrigger<MagicPlayer> {
    public MagicAtUpkeepTrigger(final int priority) {
        super(priority);
    }

    public MagicAtUpkeepTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.AtUpkeep;
    }
    
    public static MagicAtUpkeepTrigger create(final MagicSourceEvent sourceEvent) {
        return new MagicAtUpkeepTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }
    
    public static final MagicAtUpkeepTrigger YouDraw(final MagicSource staleSource, final MagicPlayer stalePlayer) {
        return new MagicAtUpkeepTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
                game.addDelayedAction(new MagicRemoveTriggerAction(this));
                return new MagicEvent(
                    game.createDelayedSource(staleSource, stalePlayer),
                    this,         
                    "PN draws a card."
                );
            }
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.doAction(new MagicDrawAction(event.getPlayer()));
            }
        }; 
    }
    
    public static final MagicAtUpkeepTrigger TwoOrMoreSpellsTransform = new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return (upkeepPlayer.getSpellsCastLastTurn() >= 2 || upkeepPlayer.getOpponent().getSpellsCastLastTurn() >= 2) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN transforms SN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            game.doAction(new MagicTransformAction(event.getPermanent()));
        }
    };
    
    public static final MagicAtUpkeepTrigger NoSpellsTransform = new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return (game.getSpellsCastLastTurn() == 0) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN transforms SN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            game.doAction(new MagicTransformAction(event.getPermanent()));
        }
    };
}
