package com.xtremelabs.robolectric.shadows;

import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.xtremelabs.robolectric.internal.Implementation;
import com.xtremelabs.robolectric.internal.Implements;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Shadow of {@code ListFragment}
 */
@Implements(ListFragment.class)
public class ShadowListFragment extends ShadowFragment {

    private ListAdapter listAdapter;
    private ListView listView;

    @Implementation
    public ListAdapter getListAdapter() {
        return listAdapter;
    }

    @Implementation
    public ListView getListView() {
        return findListView(getView());
    }

    public void setListView(ListView view) {
        listView = view;
    }

    @Implementation
    public void setListAdapter(ListAdapter listAdapter) {
        this.listAdapter = listAdapter;
        ListView lv = getListView();
        if (lv != null) {
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        Method handler = realFragment.getClass().getDeclaredMethod("onListItemClick",
                                ListView.class, View.class, int.class, long.class);
                        handler.setAccessible(true);
                        handler.invoke(realFragment, parent, view, position, id);
                    } catch (NoSuchMethodException ignored) {
                    } catch (InvocationTargetException ignored) {
                    } catch (IllegalAccessException ignored) {
                    }
                }
            });
            lv.setAdapter(listAdapter);
        }
    }

    private ListView findListView(View parent) {
        if (parent == null) {
            throw new IllegalStateException("Content view not set");
        } else if (parent instanceof ListView) {
            return (ListView) parent;
        } else {
            View listView = parent.findViewById(android.R.id.list);
            if (!(listView instanceof ListView)) {
                throw new RuntimeException("Content has view with id of android.R.id.list " +
                    "which is not a ListView");
            } else if (listView == null) {
                throw new RuntimeException("Content must have a ListView with an id of " +
                    "android.R.id.list");
            }
            return (ListView) listView;
        }
    }
}
