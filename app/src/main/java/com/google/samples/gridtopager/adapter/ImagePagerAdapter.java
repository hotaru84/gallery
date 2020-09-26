/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.gridtopager.adapter;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.google.samples.gridtopager.fragment.ImageFragment;

public class ImagePagerAdapter extends FragmentStatePagerAdapter {

  private Cursor cursor;
  private ContentResolver resolver;
  public ImagePagerAdapter(Fragment fragment) {
    // Note: Initialize with the child fragment manager.
    super(fragment.getChildFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    resolver = fragment.getContext().getContentResolver();
    updateCursor();
  }
  private Uri getUri() {
    int idColumn= cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID);
    long id = cursor.getLong(idColumn);
    return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,String.valueOf(id));
  }
  @Override
  public int getCount() {
    return cursor.getCount();
  }

  @NonNull
  @Override
  public Fragment getItem(int position) {
    cursor.moveToPosition(position);
    return ImageFragment.newInstance(getUri());
  }

  public void updateCursor() {
    this.cursor = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null,null,null,
            MediaStore.Images.Media.DATE_ADDED + " DESC");
  }
}
