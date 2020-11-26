package victor.app.tirinhas.brasil;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.ValueBar;

import org.json.JSONException;

import java.awt.font.TextAttribute;
import java.util.ArrayList;

import victor.app.tirinhas.brasil.activities.MainActivity;
import victor.app.tirinhas.brasil.adapter.MemeSelectionAdapter;
import victor.app.tirinhas.brasil.objects.Meme;
import victor.app.tirinhas.brasil.objects.Panel;
import victor.app.tirinhas.brasil.util.JSONParser;
import victor.app.tirinhas.brasil.util.MemesHelper;
import victor.app.tirinhas.brasil.util.Util;

/**
 * Created by victor on 09/08/15.
 */
public class MemesDrawerFragment extends Fragment {

    private GridView gridView;
    private Spinner spinner;
/*
    private SeekBar textSizeBar;
    private TextView text;
    private ColorPicker colorPicker;
    private ValueBar valueBar;
*/
    private Switch switchSides;

    private MemesDrawerCallbacks mCallbacks;
    private DrawerLayout mDrawerLayout;
    private View mFragmentContainerView;
    private ActionBarDrawerToggle mDrawerToggle;

    private int currentMemeCategory;
    private ArrayList<Meme> characters;

    CompoundButton.OnCheckedChangeListener switchSidesListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memes_drawer, container, false);

        gridView = (GridView) view.findViewById(R.id.gridViewMemes);
        spinner = (Spinner) view.findViewById(R.id.spinnerMemesCategory);
        switchSides = (Switch) view.findViewById(R.id.switchSides);
        currentMemeCategory = Meme.MALE;

        try {
            characters = JSONParser.getCharacters(((MainActivity) getActivity()).getJSON());
        } catch(JSONException e) {
            Log.e("MainAcitivity", "Couldn't parse JSON!");
            e.printStackTrace();
            characters = new ArrayList<Meme>();
            Toast.makeText(getActivity(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
        } catch(NullPointerException e) {
            Log.e("MainActivity", "No JSON!");
            e.printStackTrace();
            characters = new ArrayList<Meme>();
            Toast.makeText(getActivity(), R.string.check_connection, Toast.LENGTH_LONG).show();
            getActivity().finish();
        }

       switchSidesListener = new CompoundButton
                .OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCallbacks.flipPanel(isChecked);
            }
        };

        switchSides.setOnCheckedChangeListener(switchSidesListener);

        gridView.setAdapter(new MemeSelectionAdapter(getActionBar().getThemedContext(),
                MemesHelper.getCharactersByType(characters, currentMemeCategory)));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(currentMemeCategory == Meme.EXTRAS && !Util.hasPremium(getActivity()))
                    Util.buyPremiumDialog(getActivity());
                else
                    mCallbacks.onMemeSelected(MemesHelper.getCharactersByType(characters,
                            currentMemeCategory).get(position));
            }
        });

        gridView.setColumnWidth((getResources().getDisplayMetrics().density > 2.0) ? 160 : 90);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentMemeCategory = position;
                gridView.setAdapter(new MemeSelectionAdapter(getActivity(),
                        MemesHelper.getCharactersByType(characters, currentMemeCategory)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //DO Nothing
            }
        });

        return view;
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.drawable.ic_drawer,             /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (MemesDrawerCallbacks) activity;
        } catch(ClassCastException e) {
            throw new ClassCastException("Activity must implement MemesDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    public void openDrawer() {
        mDrawerLayout.openDrawer(mFragmentContainerView);
    }

    public void closeDrawer() {
        mDrawerLayout.closeDrawer(mFragmentContainerView);
    }

    public void setUpDrawerBeforeOpening(Panel panel) {
        switchSides.setOnCheckedChangeListener(null);
        switchSides.setChecked(panel.isLeft());
        switchSides.setOnCheckedChangeListener(switchSidesListener);
    }

    public static interface MemesDrawerCallbacks {

        //Called when a Meme is selected
        void onMemeSelected(Meme meme);
        void flipPanel(boolean left);

    }
}
