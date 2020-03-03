package com.dennisce.pluginstudydemo.stub;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import timber.log.Timber;

public class StubContentProvider extends ContentProvider {
    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(@NotNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return Objects.requireNonNull(getContext()).getContentResolver().query(getRealUri(uri), projection, selection, selectionArgs, sortOrder);
    }

    @Override
    public String getType(@NotNull Uri uri) {
        return null;
    }

    @Override
    public Uri insert(@NotNull Uri uri, ContentValues values) {
        return getContext().getContentResolver().insert(getRealUri(uri), values);
    }

    @Override
    public int delete(@NotNull Uri uri, String selection, String[] selectionArgs) {
        return Objects.requireNonNull(getContext()).getContentResolver().delete(getRealUri(uri), selection, selectionArgs);
    }

    @Override
    public int update(@NotNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return Objects.requireNonNull(getContext()).getContentResolver().update(getRealUri(uri), values, selection, selectionArgs);
    }

    private Uri getRealUri(Uri raw) {
        String rawAuth = raw.getAuthority();
        if (!"contentProviderHost".equals(rawAuth)) {
            Timber.w("rawAuth:%s", rawAuth);
        }

        String uriString = raw.toString();
        uriString = uriString.replaceAll(rawAuth + '/', "");
        Uri newUri = Uri.parse(uriString);
        Timber.i("realUri:%s", newUri);
        return newUri;
    }

}