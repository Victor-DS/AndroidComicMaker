package victor.app.tirinhas.brasil;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import victor.app.tirinhas.brasil.adapter.PanelsAdapter;
import victor.app.tirinhas.brasil.objects.Meme;
import victor.app.tirinhas.brasil.objects.Panel;
import victor.app.tirinhas.brasil.util.Util;

/**
 * Activities containing this fragment MUST implement the {@link OnPanelClickListener}
 * interface.
 */
public class PanelsFragment extends ListFragment {

    private final int DEFAULT_TEXT_SIZE = 25;
    private static ArrayList<Panel> panelsList;
    private OnPanelClickListener mListener;
    private PanelsAdapter adapter;

    public static PanelsFragment newInstance(ArrayList<Panel> panels) {
        PanelsFragment fragment = new PanelsFragment();
        panelsList = panels;
        return fragment;
    }

    public PanelsFragment() {
        panelsList = new ArrayList<>();
    }

    public void updateText(int position, String text, int color, int size) {
        panelsList.get(position).setText(text);
        panelsList.get(position).setTextSize(size);
        panelsList.get(position).setTextColor(color);
    }

    public void updateMeme(int position, Meme meme) {
        panelsList.get(position).setMeme(meme);
    }

    public void setPanelIsLeft(int position, boolean isLeft) {
        panelsList.get(position).setLeft(isLeft);
    }

    public void updateView(View v, int position) {
        panelsList.get(position).setPanelView(Util.getBitmapFromView(v));
    }

    public void addPanel() {
        panelsList.add(new Panel(new Meme("Default", "Do82Nfz", Meme.MALE),
                getActivity().getString(R.string.default_text), Color.BLACK, DEFAULT_TEXT_SIZE, true));
        adapter.notifyDataSetChanged();
    }

    public Panel getPanelAt(int position) {
        return panelsList.get(position);
    }

    public void removePanel() {
        panelsList.remove(panelsList.size()-1);
        adapter.notifyDataSetChanged();
    }

    public ArrayList<Panel> getAllPanels() {
        return panelsList;
    }

    public int getPanelsSize() {
        return panelsList.size();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialPanels();

        adapter = new PanelsAdapter(getActivity(), panelsList);
        setListAdapter(adapter);
    }

    private void initialPanels() {
        /*
        //Tutorial:
        panelsList.add(new Panel(new Meme("Tutorial", "Czb6WJE", Meme.MALE),
                getActivity().getString(R.string.panel_one),
                Color.BLACK, DEFAULT_TEXT_SIZE, true));

        panelsList.add(new Panel(new Meme("Tutorial", "TTXxq6c", Meme.FEMALE),
                getActivity().getString(R.string.panel_two),
                Color.BLACK, DEFAULT_TEXT_SIZE, false));

        panelsList.add(new Panel(new Meme("Tutorial", "Czb6WJE", Meme.MALE),
                getActivity().getString(R.string.panel_three),
                Color.BLACK, DEFAULT_TEXT_SIZE, true));
        panelsList.add(new Panel(new Meme("Tutorial", "Czb6WJE", Meme.MALE),
                getActivity().getString(R.string.panel_four),
                Color.BLACK, DEFAULT_TEXT_SIZE, true));

        panelsList.add(new Panel(new Meme("Tutorial", "y5GRcgs", Meme.FEMALE),
                getActivity().getString(R.string.panel_five),
                Color.BLACK, DEFAULT_TEXT_SIZE, false));

        panelsList.add(new Panel(new Meme("Tutorial", "Czb6WJE", Meme.MALE),
                getActivity().getString(R.string.panel_six),
                Color.BLACK, DEFAULT_TEXT_SIZE, true));
        panelsList.add(new Panel(new Meme("Tutorial", "Czb6WJE", Meme.MALE),
                getActivity().getString(R.string.panel_seven),
                Color.BLACK, DEFAULT_TEXT_SIZE, true));
        */

        panelsList.add(new Panel(new Meme("Initial", "Do82Nfz", Meme.MALE),
                getActivity().getString(R.string.initial_panel), Color.BLACK,
                DEFAULT_TEXT_SIZE, true));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(!Util.hasPremium(getActivity()))
            getListView().addFooterView(getFooterView());
    }

    private View getFooterView() {
        View footer = ((LayoutInflater) getActivity()
                .getSystemService(getActivity().LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.footer_ads, null, false);

        AdView mAdView = (AdView) footer.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adRequest.isTestDevice(getActivity());
        mAdView.loadAd(adRequest);

        return footer;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnPanelClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnPanelClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnPanelClickListener {
        public void onTextClickListener(int position, String originalText,
                                        int originalColor, float originalSize);
        public void onImageClickListener(int position);
    }

}
