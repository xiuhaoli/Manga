package cn.xhl.client.manga.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Spinner;

import cn.xhl.client.manga.R;

/**
 * 自定义查找dialog
 *
 * @author xiuhaoli on 2017/11/7.
 */
public class SearchDialog extends Dialog implements
        AdapterView.OnItemSelectedListener, SearchView.OnQueryTextListener {
    private SpinnerItemSelectedListener spinnerItemSelectedListener;
    private SearchListener searchListener;
    private SearchView searchView;

    public SearchDialog(@NonNull Context context) {
        super(context, R.style.SearchTheme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_search);
        setCanceledOnTouchOutside(true);
        Spinner spinner = findViewById(R.id.spinner_custom_search);
        spinner.setOnItemSelectedListener(this);
        searchView = findViewById(R.id.searchView_custom_search);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (spinnerItemSelectedListener == null) {
            return;
        }
        spinnerItemSelectedListener.selected(view, position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void setSpinnerItemSelectedListener(SpinnerItemSelectedListener spinnerItemSelectedListener) {
        this.spinnerItemSelectedListener = spinnerItemSelectedListener;
    }

    public void setSearchListener(SearchListener searchListener) {
        this.searchListener = searchListener;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (searchListener == null) {
            return false;
        }
        searchListener.search(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public void setSearchViewHint(String s) {
        searchView.setQueryHint(s);
    }

    public interface SpinnerItemSelectedListener {
        void selected(View view, int position);
    }

    public interface SearchListener {
        void search(String query);
    }
}
