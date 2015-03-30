package com.mjw.android.swipe;

/**
 * Tightly coupled listener for updating the model of a swipe views adapter.
 * Could be redesigned.
 * 
 * @author Matt Witherow
 *
 */
public class MultiChoiceSwipeListener implements SwipeListViewListener {

    private MultiSelectActionAdapter	mAdapter;

	public interface MultiSelectActionAdapter {

		public void action(int pos);

		public void actionAllSelected(int[] pos);

	}

	public MultiChoiceSwipeListener(MultiSelectActionAdapter adapter) {
		mAdapter = adapter;
	}

	@Override
	public void onDismiss(int[] reverseSortedPositions) {
		mAdapter.actionAllSelected(reverseSortedPositions);
	}

	@Override
	public void onOpened(int position, boolean toRight) {}

	@Override
	public void onClosed(int position, boolean fromRight) {}

	@Override
	public void onListChanged() {}

	@Override
	public void onMove(int position, float x) {}

	@Override
	public void onStartOpen(int position, int action, boolean right) {}

	@Override
	public void onStartClose(int position, boolean right) {}

	@Override
	public void onClickFrontView(int position) {}

	@Override
	public void onClickBackView(int position) {}

	@Override
	public int onChangeSwipeMode(int position) {
		return SwipeListView.SWIPE_MODE_DEFAULT;
	}

	@Override
	public void onChoiceChanged(int position, boolean selected) {}

	@Override
	public void onChoiceStarted() {}

	@Override
	public void onChoiceEnded() {}

	@Override
	public void onFirstListItem() {}

	@Override
	public void onLastListItem() {}

    public MultiSelectActionAdapter getAdapter() {
        return mAdapter;
    }
}
