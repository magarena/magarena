package magic.ui.screen.card.script;

import magic.ui.screen.widget.ScreenSideBar;

@SuppressWarnings("serial")
class CardSideBar extends ScreenSideBar {

    CardSideBar() {
        refreshLayout();
    }

    private void refreshLayout() {
        removeAll();
        migLayout.setLayoutConstraints("flowy, insets 0, gap 0");
        migLayout.setColumnConstraints("[fill, grow]");
        add(cardScrollPane.component());
        revalidate();
    }
}
