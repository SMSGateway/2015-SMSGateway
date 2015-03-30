package com.mjw.android.swipe;


public class DistributionDetailsSwipeListener extends MultiChoiceSwipeListener {

    private final MultiSelectBackViewActionAdapter adapter;

    public interface MultiSelectBackViewActionAdapter extends MultiSelectActionAdapter {

        public void action(int pos);

        public void actionAllSelected(int[] pos);

        public void onBackViewPressed(int position);

    }

    public DistributionDetailsSwipeListener(MultiSelectBackViewActionAdapter adapter) {
        super(adapter);
        this.adapter = adapter;
    }

    @Override
    public void onClickBackView(int position) {
        adapter.onBackViewPressed(position);
    }

}
