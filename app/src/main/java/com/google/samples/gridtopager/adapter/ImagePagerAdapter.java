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
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.samples.gridtopager.fragment.ImageFragment;

import java.util.HashMap;

public class ImagePagerAdapter extends FragmentStateAdapter {
  private HashMap<Integer, Fragment> hashMap = new HashMap<>();
  private Cursor cursor;
  private ContentResolver resolver;
  public ImagePagerAdapter(FragmentActivity fragment) {
    // Note: Initialize with the child fragment manager.
    super(fragment);
    resolver = fragment.getContentResolver();
    updateCursor();
  }
  public Fragment getFragment(int position) {return hashMap.get(position);}
  @NonNull
  @Override
  public Fragment createFragment(int position) {
    cursor.moveToPosition(position);
    Fragment f = ImageFragment.newInstance(getUri());
    hashMap.put(position,f);
    return f;
  }

  private Uri getUri() {
    int idColumn= cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID);
    long id = cursor.getLong(idColumn);
    return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,String.valueOf(id));
  }

  public void updateCursor() {
    this.cursor = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null,null,null,
            MediaStore.Images.Media.DATE_ADDED + " DESC");
  }

  @Override
  public int getItemCount() {
    return cursor.getCount();
  }
}
