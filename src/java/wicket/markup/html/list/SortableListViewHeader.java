/*
 * $Id$
 * $Revision$ $Date$
 * 
 * ==================================================================== Licensed
 * under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the
 * License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.markup.html.list;

import java.util.Collections;
import java.util.Comparator;

import wicket.markup.ComponentTag;
import wicket.markup.html.border.Border;
import wicket.markup.html.link.Link;

/**
 * Sortable list view header component for a single list view column.
 * Functionality provided includes sorting the underlying list view and changing
 * the colours (style) of the header.
 * 
 * @see SortableListViewHeaderGroup
 * @see SortableListViewHeaders
 * @author Juergen Donnerstag
 */
public abstract class SortableListViewHeader extends Border
{
	/** Sort ascending or descending */
	private boolean ascending;
	
	/** All sortable columns of a single list view are grouped */
	private final SortableListViewHeaderGroup group;

	/**
	 * Construct.
	 * 
	 * @param componentName
	 *            The component name
	 * @param group
	 *            The group of headers the new one will be added to
	 */
	public SortableListViewHeader(final String componentName,
			final SortableListViewHeaderGroup group)
	{
		super(componentName);

		// Default to descending.
		this.ascending = false;
		this.group = group;

		// If user clicks on the header, sorting will reverse
		add(new Link("actionLink")
		{
			public void onLinkClicked()
			{
				// change sorting order: ascending <-> descending
				ascending = !ascending;

				// Tell the header group that something has changed
				group.setSortedColumn(getName());

				// sort the list view's model data accordingly
				sort();

				// Redirect back to result
				getRequestCycle().setRedirect(true);
			}
		});
	}

	/**
	 * Compare two objects (list elements of list view's model object). Both
	 * objects must implement Comparable. In order to compare basic types like
	 * int or double, simply subclass the method.
	 * 
	 * @param o1
	 *            first object
	 * @param o2
	 *            second object
	 * @return comparision result
	 */
	protected int compare(Object o1, Object o2)
	{
		return ((Comparable)o1).compareTo((Comparable)o2);
	}

	/**
	 * Get CSS style for the header based on ascending / descending. Delegate to
	 * the header group to determine the style.
	 * 
	 * @return css class
	 */
	protected final String getCssClass()
	{
		// TODO This needs to be integrated with our CSS design
		return group.getCssClass(getName(), ascending);
	}

	/**
	 * Delegate to the header group to handle the tag.
	 * 
	 * @see wicket.Component#onComponentTag(wicket.markup.ComponentTag)
	 * @param tag
	 *            The current ComponentTag to handle
	 */
	protected void onComponentTag(final ComponentTag tag)
	{
		group.onComponentTag(tag, getCssClass());
	}

	/**
	 * Sort list view's model object based on column data
	 */
	protected void sort()
	{
		Collections.sort(group.getList(), new Comparator()
		{
			public int compare(Object o1, Object o2)
			{
				if (ascending)
				{
					return compare(o1, o2);
				}
				else
				{
					return compare(o2, o1);
				}
			}
		});
	}
}
