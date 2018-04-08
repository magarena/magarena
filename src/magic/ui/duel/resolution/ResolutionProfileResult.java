package magic.ui.duel.resolution;

import java.awt.Font;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

public class ResolutionProfileResult {

    private final Map<ResolutionProfileType,Object> results;

    public ResolutionProfileResult() {
        results= new HashMap<>();
    }

    public void setBoundary(final ResolutionProfileType type,final Rectangle boundary) {
        results.put(type,boundary);
    }

    public Rectangle getBoundary(final ResolutionProfileType type) {
        return (Rectangle)results.get(type);
    }

    public void setFont(final ResolutionProfileType type,final Font font) {
        results.put(type,font);
    }

    public Font getFont(final ResolutionProfileType type) {
        return (Font)results.get(type);
    }

    public void setFlag(final ResolutionProfileType type,final boolean flag) {
        results.put(type,flag);
    }

    public boolean getFlag(final ResolutionProfileType type) {
        return (Boolean)results.get(type);
    }
}
