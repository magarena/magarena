package magic.ui;

import java.util.List;

import magic.ui.widget.MenuButton;

public interface IMagActionBar {
    MenuButton getLeftAction();
    MenuButton getRightAction();
    List<MenuButton> getMiddleActions();
}
