/*
 * Copyright (c) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.turboshow.tv.ui;

import android.os.Bundle;
import androidx.leanback.app.VerticalGridSupportFragment;
import androidx.leanback.widget.*;
import cn.turboshow.tv.browse.BrowseItem;
import cn.turboshow.tv.ui.presenter.GridItemPresenter;

public class VerticalGridFragment extends VerticalGridSupportFragment {
    private static final int NUM_COLUMNS = 5;
    private ArrayObjectAdapter itemsAdapter = new ArrayObjectAdapter(new GridItemPresenter());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("test");
        setAdapter(itemsAdapter);
        itemsAdapter.add(new BrowseItem("test", () -> null));
        setupFragment();
    }

    private void setupFragment() {
        VerticalGridPresenter gridPresenter = new VerticalGridPresenter();
        gridPresenter.setNumberOfColumns(NUM_COLUMNS);
        setGridPresenter(gridPresenter);

        setOnItemViewClickedListener(new ItemViewClickedListener());
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {
        }
    }
}
