package list_view;

/**
 * Call back listener for list fragment, invoked by adapter.
 */
public interface ListAdapterCallback {
    void onListItemClicked(int position);
}
