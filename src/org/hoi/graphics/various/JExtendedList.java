package org.hoi.graphics.various;

import org.hoi.various.ListUtils;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class JExtendedList<T> extends JList<T> {
    public JExtendedList (List<T> values) {
        super(ListUtils.toListModel(values));
        this.setCellRenderer(new SimpleCellRenderer());
    }

    public String getTextFor (T value) {
        return value.toString();
    }

    public class SimpleCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel cell = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            cell.setText(getTextFor((T) value));

            return cell;
        }
    }
}
