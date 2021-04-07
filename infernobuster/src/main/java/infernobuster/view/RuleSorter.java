package infernobuster.view;

import java.util.Comparator;

import javax.swing.SortOrder;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import infernobuster.model.Model;

public class RuleSorter<T extends TableModel> extends TableRowSorter<T>{
	public RuleSorter(T model) {
		super(model);
	}

	public Comparator<?> getComparator(final int column) {
		if(column == Model.BADGE_INDEX) {
			return new BadgeSorter();
		}
		
		Comparator c = new Comparator() {
			@Override
			public int compare(Object o1, Object o2) {
				boolean ascending = getSortKeys().get(0).getSortOrder() == SortOrder.ASCENDING;
                if (o1 instanceof Boolean) {
                	System.out.println("asdsa");
                    if(ascending && (Boolean) o1)
                        return -1;
                    else
                        return 1;
                } else if (o2 instanceof Boolean) {
                    if(ascending  && (Boolean) o1)
                        return 1;
                    else
                        return -1;
                }
                else {
                    return ((Comparable<Object>) o1).compareTo(o2);
                }
			}
		};
		
		return c;
	}
}
